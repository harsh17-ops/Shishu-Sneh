'use client'

import { useEffect, useState } from 'react'
import { useRouter, useParams } from 'next/navigation'
import { supabase } from '@/app/lib/supabase'
import { WeightEntry } from '@/app/lib/types'
import Link from 'next/link'

export default function EditWeight() {
  const params = useParams()
  const babyId = params.id as string
  const weightId = params.weightId as string
  const [weight, setWeight] = useState<WeightEntry | null>(null)
  const [weightKg, setWeightKg] = useState('')
  const [recordedDate, setRecordedDate] = useState('')
  const [notes, setNotes] = useState('')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const router = useRouter()

  useEffect(() => {
    const fetchWeight = async () => {
      try {
        const { data, error: fetchError } = await supabase
          .from('weight_entries')
          .select('*')
          .eq('id', weightId)
          .eq('baby_id', babyId)
          .single()

        if (fetchError || !data) {
          router.push(`/babies/${babyId}`)
          return
        }

        setWeight(data)
        setWeightKg(data.weight_kg.toString())
        setRecordedDate(data.recorded_date)
        setNotes(data.notes || '')
      } catch (error) {
        console.error('Error fetching weight:', error)
      } finally {
        setLoading(false)
      }
    }

    if (weightId && babyId) fetchWeight()
  }, [weightId, babyId, router])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSaving(true)
    setError(null)

    try {
      const { error: updateError } = await supabase
        .from('weight_entries')
        .update({
          weight_kg: parseFloat(weightKg),
          recorded_date: recordedDate,
          notes: notes || null,
          updated_at: new Date().toISOString(),
        })
        .eq('id', weightId)

      if (updateError) throw updateError
      router.push(`/babies/${babyId}`)
    } catch (error: any) {
      setError(error.message || 'Failed to update weight')
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-foreground/70">Loading...</p>
        </div>
      </div>
    )
  }

  if (!weight) {
    return (
      <div className="text-center py-12">
        <p className="text-foreground/70 mb-4">Weight entry not found</p>
        <Link href={`/babies/${babyId}`} className="text-primary hover:underline">
          Back to Baby
        </Link>
      </div>
    )
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex items-center gap-4 mb-8">
        <Link href={`/babies/${babyId}`} className="text-primary hover:underline">
          ← Back to Baby
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-8">
        <h1 className="text-3xl font-bold text-foreground mb-6">Edit Weight Entry</h1>

        {error && (
          <div className="mb-4 p-3 bg-destructive/10 text-destructive text-sm rounded-lg">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Weight (kg) *
              </label>
              <input
                type="number"
                step="0.1"
                value={weightKg}
                onChange={(e) => setWeightKg(e.target.value)}
                className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Date *
              </label>
              <input
                type="date"
                value={recordedDate}
                onChange={(e) => setRecordedDate(e.target.value)}
                className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-2">
              Notes
            </label>
            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary resize-none"
              rows={4}
            />
          </div>

          <div className="flex gap-4 pt-6">
            <button
              type="submit"
              disabled={saving || !weightKg || !recordedDate}
              className="flex-1 px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 disabled:opacity-50 transition font-semibold"
            >
              {saving ? 'Saving...' : 'Save Changes'}
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
