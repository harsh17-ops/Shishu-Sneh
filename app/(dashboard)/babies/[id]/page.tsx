'use client'

import { useEffect, useState } from 'react'
import { useRouter, useParams } from 'next/navigation'
import { supabase } from '@/app/lib/supabase'
import { Baby, Vaccination, WeightEntry, Milestone } from '@/app/lib/types'
import { Plus, Trash2 } from 'lucide-react'
import Link from 'next/link'

export default function BabyDetail() {
  const params = useParams()
  const babyId = params.id as string
  const [baby, setBaby] = useState<Baby | null>(null)
  const [vaccinations, setVaccinations] = useState<Vaccination[]>([])
  const [weights, setWeights] = useState<WeightEntry[]>([])
  const [milestones, setMilestones] = useState<Milestone[]>([])
  const [activeTab, setActiveTab] = useState<'vaccinations' | 'weight' | 'milestones'>('vaccinations')
  const [loading, setLoading] = useState(true)
  const router = useRouter()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const {
          data: { user },
        } = await supabase.auth.getUser()

        if (!user) {
          router.push('/signin')
          return
        }

        // Fetch baby details
        const { data: babyData, error: babyError } = await supabase
          .from('babies')
          .select('*')
          .eq('id', babyId)
          .eq('user_id', user.id)
          .single()

        if (babyError || !babyData) {
          router.push('/babies')
          return
        }

        setBaby(babyData)

        // Fetch vaccinations
        const { data: vacsData, error: vacsError } = await supabase
          .from('vaccinations')
          .select('*')
          .eq('baby_id', babyId)
          .order('scheduled_date', { ascending: true })

        if (!vacsError) setVaccinations(vacsData || [])

        // Fetch weight entries
        const { data: weightsData, error: weightsError } = await supabase
          .from('weight_entries')
          .select('*')
          .eq('baby_id', babyId)
          .order('recorded_date', { ascending: true })

        if (!weightsError) setWeights(weightsData || [])

        // Fetch milestones
        const { data: milestonesData, error: milestonesError } = await supabase
          .from('milestones')
          .select('*')
          .eq('baby_id', babyId)
          .order('milestone_age_months', { ascending: true })

        if (!milestonesError) setMilestones(milestonesData || [])
      } catch (error) {
        console.error('Error fetching baby details:', error)
      } finally {
        setLoading(false)
      }
    }

    if (babyId) fetchData()
  }, [babyId, router])

  const handleDeleteVaccination = async (vaccineId: string) => {
    if (!window.confirm('Delete this vaccination record?')) return

    try {
      const { error } = await supabase
        .from('vaccinations')
        .delete()
        .eq('id', vaccineId)

      if (error) throw error
      setVaccinations(vaccinations.filter((v) => v.id !== vaccineId))
    } catch (error) {
      console.error('Error deleting vaccination:', error)
      alert('Failed to delete vaccination')
    }
  }

  const handleDeleteWeight = async (weightId: string) => {
    if (!window.confirm('Delete this weight entry?')) return

    try {
      const { error } = await supabase
        .from('weight_entries')
        .delete()
        .eq('id', weightId)

      if (error) throw error
      setWeights(weights.filter((w) => w.id !== weightId))
    } catch (error) {
      console.error('Error deleting weight:', error)
      alert('Failed to delete weight entry')
    }
  }

  const handleDeleteMilestone = async (milestoneId: string) => {
    if (!window.confirm('Delete this milestone?')) return

    try {
      const { error } = await supabase
        .from('milestones')
        .delete()
        .eq('id', milestoneId)

      if (error) throw error
      setMilestones(milestones.filter((m) => m.id !== milestoneId))
    } catch (error) {
      console.error('Error deleting milestone:', error)
      alert('Failed to delete milestone')
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

  if (!baby) {
    return (
      <div className="text-center py-12">
        <p className="text-foreground/70 mb-4">Baby not found</p>
        <Link href="/babies" className="text-primary hover:underline">
          Back to Babies
        </Link>
      </div>
    )
  }

  const babyAge = Math.floor(
    (Date.now() - new Date(baby.date_of_birth).getTime()) / (365.25 * 24 * 60 * 60 * 1000)
  )

  return (
    <div className="space-y-8">
      <div className="flex items-center gap-4 mb-8">
        <Link href="/babies" className="text-primary hover:underline">
          ← Back to Babies
        </Link>
      </div>

      {/* Baby Header */}
      <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg shadow-sm border border-primary/20 p-8">
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-4xl font-bold text-foreground mb-2">{baby.name}</h1>
            <p className="text-lg text-foreground/70 mb-4">
              Age: {babyAge} {babyAge === 1 ? 'year' : 'years'} old
            </p>
            <div className="space-y-2 text-foreground/70">
              <p>Date of Birth: {new Date(baby.date_of_birth).toLocaleDateString()}</p>
              {baby.gender && <p className="capitalize">Gender: {baby.gender}</p>}
              {baby.blood_type && <p>Blood Type: {baby.blood_type}</p>}
            </div>
          </div>
          <Link
            href={`/babies/${baby.id}/edit`}
            className="px-4 py-2 border border-primary text-primary rounded-lg hover:bg-primary/5 transition"
          >
            Edit
          </Link>
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-foreground/10">
        <div className="flex gap-8">
          <button
            onClick={() => setActiveTab('vaccinations')}
            className={`py-4 px-2 border-b-2 font-medium transition ${
              activeTab === 'vaccinations'
                ? 'border-primary text-primary'
                : 'border-transparent text-foreground/70 hover:text-foreground'
            }`}
          >
            Vaccinations ({vaccinations.length})
          </button>
          <button
            onClick={() => setActiveTab('weight')}
            className={`py-4 px-2 border-b-2 font-medium transition ${
              activeTab === 'weight'
                ? 'border-primary text-primary'
                : 'border-transparent text-foreground/70 hover:text-foreground'
            }`}
          >
            Weight ({weights.length})
          </button>
          <button
            onClick={() => setActiveTab('milestones')}
            className={`py-4 px-2 border-b-2 font-medium transition ${
              activeTab === 'milestones'
                ? 'border-primary text-primary'
                : 'border-transparent text-foreground/70 hover:text-foreground'
            }`}
          >
            Milestones ({milestones.length})
          </button>
        </div>
      </div>

      {/* Content */}
      <div className="space-y-6">
        {activeTab === 'vaccinations' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold text-foreground">Vaccinations</h2>
              <Link
                href={`/babies/${baby.id}/vaccinations/new`}
                className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
              >
                <Plus className="w-4 h-4" />
                Add Vaccination
              </Link>
            </div>

            {vaccinations.length === 0 ? (
              <div className="text-center py-8 bg-white rounded-lg border border-foreground/10">
                <p className="text-foreground/70 mb-4">No vaccinations recorded yet</p>
                <Link
                  href={`/babies/${baby.id}/vaccinations/new`}
                  className="inline-block px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
                >
                  Add Vaccination
                </Link>
              </div>
            ) : (
              <div className="grid gap-4">
                {vaccinations.map((vaccine) => (
                  <div
                    key={vaccine.id}
                    className="bg-white rounded-lg border border-foreground/10 p-4 flex items-start justify-between"
                  >
                    <div className="flex-1">
                      <h3 className="font-semibold text-foreground">{vaccine.vaccine_name}</h3>
                      <p className="text-sm text-foreground/70 mt-1">
                        Scheduled: {new Date(vaccine.scheduled_date).toLocaleDateString()}
                      </p>
                      {vaccine.administered_date && (
                        <p className="text-sm text-green-600 mt-1">
                          Administered: {new Date(vaccine.administered_date).toLocaleDateString()}
                        </p>
                      )}
                      <p className={`text-sm font-medium mt-2 ${
                        vaccine.status === 'administered' ? 'text-green-600' :
                        vaccine.status === 'missed' ? 'text-destructive' :
                        'text-amber-600'
                      }`}>
                        Status: {vaccine.status.charAt(0).toUpperCase() + vaccine.status.slice(1)}
                      </p>
                    </div>
                    <div className="flex gap-2">
                      <Link
                        href={`/babies/${baby.id}/vaccinations/${vaccine.id}/edit`}
                        className="px-3 py-1 text-sm border border-primary text-primary rounded hover:bg-primary/5 transition"
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDeleteVaccination(vaccine.id)}
                        className="p-2 text-destructive hover:bg-destructive/10 rounded transition"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'weight' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold text-foreground">Weight Tracking</h2>
              <Link
                href={`/babies/${baby.id}/weight/new`}
                className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
              >
                <Plus className="w-4 h-4" />
                Add Weight
              </Link>
            </div>

            {weights.length === 0 ? (
              <div className="text-center py-8 bg-white rounded-lg border border-foreground/10">
                <p className="text-foreground/70 mb-4">No weight entries recorded yet</p>
                <Link
                  href={`/babies/${baby.id}/weight/new`}
                  className="inline-block px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
                >
                  Add Weight
                </Link>
              </div>
            ) : (
              <div className="grid gap-4">
                {weights.map((weight) => (
                  <div
                    key={weight.id}
                    className="bg-white rounded-lg border border-foreground/10 p-4 flex items-start justify-between"
                  >
                    <div className="flex-1">
                      <h3 className="font-semibold text-foreground">{weight.weight_kg} kg</h3>
                      <p className="text-sm text-foreground/70 mt-1">
                        Date: {new Date(weight.recorded_date).toLocaleDateString()}
                      </p>
                      {weight.notes && (
                        <p className="text-sm text-foreground/70 mt-2">{weight.notes}</p>
                      )}
                    </div>
                    <div className="flex gap-2">
                      <Link
                        href={`/babies/${baby.id}/weight/${weight.id}/edit`}
                        className="px-3 py-1 text-sm border border-primary text-primary rounded hover:bg-primary/5 transition"
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDeleteWeight(weight.id)}
                        className="p-2 text-destructive hover:bg-destructive/10 rounded transition"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'milestones' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold text-foreground">Milestones</h2>
              <Link
                href={`/babies/${baby.id}/milestones/new`}
                className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
              >
                <Plus className="w-4 h-4" />
                Add Milestone
              </Link>
            </div>

            {milestones.length === 0 ? (
              <div className="text-center py-8 bg-white rounded-lg border border-foreground/10">
                <p className="text-foreground/70 mb-4">No milestones recorded yet</p>
                <Link
                  href={`/babies/${baby.id}/milestones/new`}
                  className="inline-block px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
                >
                  Add Milestone
                </Link>
              </div>
            ) : (
              <div className="grid gap-4">
                {milestones.map((milestone) => (
                  <div
                    key={milestone.id}
                    className={`rounded-lg border p-4 flex items-start justify-between ${
                      milestone.achieved
                        ? 'bg-green-50 border-green-200'
                        : 'bg-white border-foreground/10'
                    }`}
                  >
                    <div className="flex-1">
                      <h3 className="font-semibold text-foreground">{milestone.milestone_name}</h3>
                      <p className="text-sm text-foreground/70 mt-1">
                        Age: {milestone.milestone_age_months} months
                      </p>
                      {milestone.achieved && milestone.achieved_date && (
                        <p className="text-sm text-green-600 mt-1 font-medium">
                          Achieved: {new Date(milestone.achieved_date).toLocaleDateString()}
                        </p>
                      )}
                      {milestone.notes && (
                        <p className="text-sm text-foreground/70 mt-2">{milestone.notes}</p>
                      )}
                    </div>
                    <div className="flex gap-2">
                      <Link
                        href={`/babies/${baby.id}/milestones/${milestone.id}/edit`}
                        className="px-3 py-1 text-sm border border-primary text-primary rounded hover:bg-primary/5 transition"
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDeleteMilestone(milestone.id)}
                        className="p-2 text-destructive hover:bg-destructive/10 rounded transition"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
