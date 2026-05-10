'use client'

import { useEffect, useState } from 'react'
import { supabase } from '@/app/lib/supabase'
import { Baby, Vaccination } from '@/app/lib/types'
import { AlertCircle, Calendar } from 'lucide-react'
import Link from 'next/link'

export default function Dashboard() {
  const [babies, setBabies] = useState<Baby[]>([])
  const [upcomingVaccines, setUpcomingVaccines] = useState<
    (Vaccination & { baby_name: string })[]
  >([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const {
          data: { user },
        } = await supabase.auth.getUser()

        if (!user) return

        // Fetch babies
        const { data: babiesData, error: babiesError } = await supabase
          .from('babies')
          .select('*')
          .eq('user_id', user.id)
          .order('created_at', { ascending: false })

        if (babiesError) throw babiesError
        setBabies(babiesData || [])

        // Fetch upcoming vaccinations
        if (babiesData && babiesData.length > 0) {
          const babyIds = babiesData.map((b) => b.id)
          const { data: vaccinesData, error: vaccinesError } = await supabase
            .from('vaccinations')
            .select('*')
            .in('baby_id', babyIds)
            .eq('status', 'pending')
            .lte('scheduled_date', new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0])
            .order('scheduled_date', { ascending: true })

          if (vaccinesError) throw vaccinesError

          const vaccinesWithNames = (vaccinesData || []).map((vaccine) => ({
            ...vaccine,
            baby_name: babiesData.find((b) => b.id === vaccine.baby_id)?.name || 'Unknown',
          }))

          setUpcomingVaccines(vaccinesWithNames)
        }
      } catch (error) {
        console.error('Error fetching dashboard data:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [])

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-foreground/70">Loading dashboard...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div>
        <h1 className="text-4xl font-bold text-foreground mb-2">Welcome to Shishu Sneh</h1>
        <p className="text-foreground/70">Track and monitor your baby&apos;s health and development</p>
      </div>

      {/* Babies Overview */}
      <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-foreground">Your Babies</h2>
          <Link
            href="/babies/new"
            className="px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition font-medium"
          >
            Add Baby
          </Link>
        </div>

        {babies.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-foreground/70 mb-4">No babies added yet</p>
            <Link
              href="/babies/new"
              className="inline-block px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
            >
              Add Your First Baby
            </Link>
          </div>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {babies.map((baby) => (
              <Link
                key={baby.id}
                href={`/babies/${baby.id}`}
                className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg p-6 border border-primary/20 hover:shadow-md transition cursor-pointer"
              >
                <h3 className="text-xl font-semibold text-foreground mb-2">{baby.name}</h3>
                <p className="text-sm text-foreground/70">
                  DOB: {new Date(baby.date_of_birth).toLocaleDateString()}
                </p>
                {baby.gender && (
                  <p className="text-sm text-foreground/70 capitalize">Gender: {baby.gender}</p>
                )}
              </Link>
            ))}
          </div>
        )}
      </div>

      {/* Upcoming Vaccinations */}
      {upcomingVaccines.length > 0 && (
        <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6">
          <h2 className="text-2xl font-bold text-foreground mb-6 flex items-center gap-2">
            <AlertCircle className="w-6 h-6 text-amber-500" />
            Upcoming Vaccinations
          </h2>

          <div className="space-y-4">
            {upcomingVaccines.map((vaccine) => (
              <div
                key={vaccine.id}
                className="bg-amber-50 border border-amber-200 rounded-lg p-4 flex items-start justify-between"
              >
                <div className="flex-1">
                  <p className="font-semibold text-foreground">{vaccine.vaccine_name}</p>
                  <p className="text-sm text-foreground/70">{vaccine.baby_name}</p>
                  <div className="flex items-center gap-2 mt-2 text-sm text-amber-700">
                    <Calendar className="w-4 h-4" />
                    {new Date(vaccine.scheduled_date).toLocaleDateString()}
                  </div>
                </div>
                <Link
                  href={`/babies/${vaccine.baby_id}`}
                  className="px-3 py-1 bg-amber-500 text-white rounded text-sm hover:opacity-90 transition"
                >
                  View
                </Link>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Quick Stats */}
      {babies.length > 0 && (
        <div className="grid md:grid-cols-3 gap-6">
          <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6 text-center">
            <p className="text-3xl font-bold text-primary mb-2">{babies.length}</p>
            <p className="text-foreground/70">Babies Tracked</p>
          </div>
          <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6 text-center">
            <p className="text-3xl font-bold text-primary mb-2">{upcomingVaccines.length}</p>
            <p className="text-foreground/70">Upcoming Vaccinations</p>
          </div>
          <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6 text-center">
            <p className="text-3xl font-bold text-primary mb-2">100%</p>
            <p className="text-foreground/70">Data Privacy</p>
          </div>
        </div>
      )}
    </div>
  )
}
