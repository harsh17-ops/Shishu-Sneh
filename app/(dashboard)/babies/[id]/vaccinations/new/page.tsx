'use client'

import { useState } from 'react'
import { useRouter, useParams } from 'next/navigation'
import { supabase } from '@/app/lib/supabase'
import Link from 'next/link'

export default function AddVaccination() {
  const params = useParams()
  const babyId = params.id as string
  const [vaccineName, setVaccineName] = useState('')
  const [scheduledDate, setScheduledDate] = useState('')
  const [administeredDate, setAdministeredDate] = useState('')
  const [status, setStatus] = useState<'pending' | 'administered' | 'missed'>('pending')
  const [notes, setNotes] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const router = useRouter()

  const commonVaccines = [
    'BCG',
    'Hepatitis B',
    'Polio (IPV)',
    'Diphtheria, Pertussis, Tetanus (DPT)',
    'Pneumococcal (PCV)',
    'Rotavirus',
    'Measles, Mumps, Rubella (MMR)',
    'Varicella',
    'Japanese Encephalitis',
    'Typhoid',
  ]

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      const { error: insertError } = await supabase
        .from('vaccinations')
        .insert([
          {
            baby_id: babyId,
            vaccine_name: vaccineName,
            scheduled_date: scheduledDate,
            administered_date: administeredDate || null,
            status,
            notes: notes || null,
          },
        ])

      if (insertError) throw insertError
      router.push(`/babies/${babyId}`)
    } catch (error: any) {
      setError(error.message || 'Failed to add vaccination')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex items-center gap-4 mb-8">
        <Link href={`/babies/${babyId}`} className="text-primary hover:underline">
          ← Back to Baby
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-8">
        <h1 className="text-3xl font-bold text-foreground mb-6">Add Vaccination</h1>

        {error && (
          <div className="mb-4 p-3 bg-destructive/10 text-destructive text-sm rounded-lg">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">
              Vaccine Name *
            </label>
            <select
              value={vaccineName}
              onChange={(e) => setVaccineName(e.target.value)}
              className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            >
              <option value="">Select vaccine</option>
              {commonVaccines.map((vaccine) => (
                <option key={vaccine} value={vaccine}>
                  {vaccine}
                </option>
              ))}
              <option value="">--- Custom ---</option>
            </select>
            {vaccineName && !commonVaccines.includes(vaccineName) && (
              <input
                type="text"
                value={vaccineName}
                onChange={(e) => setVaccineName(e.target.value)}
                placeholder="Enter custom vaccine name"
                className="w-full mt-2 px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            )}
          </div>

          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Scheduled Date *
              </label>
              <input
                type="date"
                value={scheduledDate}
                onChange={(e) => setScheduledDate(e.target.value)}
                className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Status
              </label>
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value as any)}
                className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              >
                <option value="pending">Pending</option>
                <option value="administered">Administered</option>
                <option value="missed">Missed</option>
              </select>
            </div>
          </div>

          {status === 'administered' && (
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Date Administered
              </label>
              <input
                type="date"
                value={administeredDate}
                onChange={(e) => setAdministeredDate(e.target.value)}
                className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-foreground mb-2">
              Notes
            </label>
            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Add any additional notes"
              className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary resize-none"
              rows={4}
            />
          </div>

          <div className="flex gap-4 pt-6">
            <button
              type="submit"
              disabled={loading || !vaccineName || !scheduledDate}
              className="flex-1 px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 disabled:opacity-50 transition font-semibold"
            >
              {loading ? 'Adding...' : 'Add Vaccination'}
            </button>
            <Link
              href={`/babies/${babyId}`}
              className="flex-1 px-6 py-2 border border-foreground/20 text-foreground rounded-lg hover:bg-foreground/5 transition text-center font-semibold"
            >
              Cancel
            </Link>
          </div>
        </form>
      </div>
    </div>
  )
}
