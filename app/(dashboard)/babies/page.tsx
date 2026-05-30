'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { supabase } from '@/app/lib/supabase'
import { Baby } from '@/app/lib/types'
import { Trash2 } from 'lucide-react'
import Link from 'next/link'

export default function BabiesPage() {
  const [babies, setBabies] = useState<Baby[]>([])
  const [loading, setLoading] = useState(true)
  const [deleting, setDeleting] = useState<string | null>(null)
  const router = useRouter()

  useEffect(() => {
    const fetchBabies = async () => {
      try {
        const {
          data: { user },
        } = await supabase.auth.getUser()

        if (!user) {
          router.push('/signin')
          return
        }

        const { data, error } = await supabase
          .from('babies')
          .select('*')
          .eq('user_id', user.id)
          .order('created_at', { ascending: false })

        if (error) throw error
        setBabies(data || [])
      } catch (error) {
        console.error('Error fetching babies:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchBabies()
  }, [router])

  const handleDelete = async (babyId: string) => {
    if (!window.confirm('Are you sure you want to delete this baby profile? This action cannot be undone.')) {
      return
    }

    setDeleting(babyId)
    try {
      const { error } = await supabase
        .from('babies')
        .delete()
        .eq('id', babyId)

      if (error) throw error
      setBabies(babies.filter((b) => b.id !== babyId))
    } catch (error) {
      console.error('Error deleting baby:', error)
      alert('Failed to delete baby profile')
    } finally {
      setDeleting(null)
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-foreground/70">Loading babies...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      <div className="flex justify-between items-center">
        <h1 className="text-4xl font-bold text-foreground">My Babies</h1>
        <Link
          href="/babies/new"
          className="px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition font-medium"
        >
          Add Baby
        </Link>
      </div>

      {babies.length === 0 ? (
        <div className="bg-white rounded-lg shadow-sm border border-foreground/10 p-12 text-center">
          <p className="text-foreground/70 mb-4 text-lg">No babies added yet</p>
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
            <div
              key={baby.id}
              className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6 hover:shadow-md transition"
            >
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="text-xl font-semibold text-foreground">{baby.name}</h3>
                  <p className="text-sm text-foreground/70">
                    DOB: {new Date(baby.date_of_birth).toLocaleDateString()}
                  </p>
                </div>
                <button
                  onClick={() => handleDelete(baby.id)}
                  disabled={deleting === baby.id}
                  className="p-2 text-destructive hover:bg-destructive/10 rounded transition disabled:opacity-50"
                >
                  <Trash2 className="w-5 h-5" />
                </button>
              </div>

              {baby.gender && (
                <p className="text-sm text-foreground/70 mb-2 capitalize">Gender: {baby.gender}</p>
              )}
              {baby.blood_type && (
                <p className="text-sm text-foreground/70 mb-4">Blood Type: {baby.blood_type}</p>
              )}

              <div className="flex gap-2">
                <Link
                  href={`/babies/${baby.id}`}
                  className="flex-1 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition text-center font-medium"
                >
                  View Details
                </Link>
                <Link
                  href={`/babies/${baby.id}/edit`}
                  className="flex-1 px-4 py-2 border border-primary text-primary rounded-lg hover:bg-primary/5 transition text-center font-medium"
                >
                  Edit
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
