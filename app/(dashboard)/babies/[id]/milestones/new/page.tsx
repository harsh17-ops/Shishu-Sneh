'use client'

import { useState } from 'react'
import { useRouter, useParams } from 'next/navigation'
import { supabase } from '@/app/lib/supabase'
import Link from 'next/link'

export default function AddMilestone() {
  const params = useParams()
  const babyId = params.id as string
  const [milestoneName, setMilestoneName] = useState('')
  const [milestoneAgeMonths, setMilestoneAgeMonths] = useState('')
  const [achieved, setAchieved] = useState(false)
  const [achievedDate, setAchievedDate] = useState('')
  const [notes, setNotes] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const router = useRouter()

  const commonMilestones = [
    'First smile',
    'Cooing and babbling',
    'Holding head steady',
    'Rolling over',
    'Sitting without support',
    'First tooth',
    'Crawling',
    'Standing with support',
    'First words',
    'Walking with support',
    'Walking independently',
    'Saying simple sentences',
  ]

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      const { error: insertError } = await supabase
        .from('milestones')
        .insert([
          {
            baby_id: babyId,
            milestone_name: milestoneName,
            milestone_age_months: parseInt(milestoneAgeMonths),
            achieved,
            achieved_date: achieved && achievedDate ? achievedDate : null,
            notes: notes || null,
          },
        ])

      if (insertError) throw insertError
      router.push(`/babies/${babyId}`)
    } catch (error: any) {
      setError(error.message || 'Failed to add milestone')
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
        <h1 className="text-3xl font-bold text-foreground mb-6">Add Milestone</h1>

        {error && (
          <div className="mb-4 p-3 bg-destructive/10 text-destructive text-sm rounded-lg">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">
              Milestone Name *
            </label>
            <select
              value={milestoneName}
              onChange={(e) => setMilestoneName(e.target.value)}
              className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            >
              <option value="">Select milestone</option>
              {commonMilestones.map((milestone) => (
                <option key={milestone} value={milestone}>
                  {milestone}
                </option>
              ))}
            </select>
            {milestoneName && !commonMilestones.includes(milestoneName) && (
              <input
                type="text"
                value={milestoneName}
                onChange={(e) => setMilestoneName(e.target.value)}
                placeholder="Enter custom milestone"
                className="w-full mt-2 px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            )}
          </div>

          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Age (months) *
              </label>
              <input
                type="number"
                value={milestoneAgeMonths}
                onChange={(e) => setMilestoneAgeMonths(e.target.value)}
                className="w-full px-4 py-2 border border-foreground/20 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                placeholder="e.g., 6"
                required
              />
            </div>

            <div className="flex items-end">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={achieved}
                  onChange={(e) => setAchieved(e.target.checked)}
                  className="w-5 h-5 rounded border-foreground/20 text-primary focus:ring-2 focus:ring-primary"
                />
                <span className="text-sm font-medium text-foreground">Milestone Achieved</span>
              </label>
            </div>
          </div>

          {achieved && (
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">
                Date Achieved
              </label>
              <input
                type="date"
                value={achievedDate}
                onChange={(e) => setAchievedDate(e.target.value)}
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
              disabled={loading || !milestoneName || !milestoneAgeMonths}
              className="flex-1 px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 disabled:opacity-50 transition font-semibold"
            >
              {loading ? 'Adding...' : 'Add Milestone'}
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
