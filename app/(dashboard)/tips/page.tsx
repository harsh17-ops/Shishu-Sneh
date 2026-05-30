'use client'

import { useEffect, useState } from 'react'
import { supabase } from '@/app/lib/supabase'
import { Tip } from '@/app/lib/types'
import { BookOpen } from 'lucide-react'

export default function TipsPage() {
  const [tips, setTips] = useState<Tip[]>([])
  const [selectedCategory, setSelectedCategory] = useState<string>('all')
  const [loading, setLoading] = useState(true)

  const categories = ['feeding', 'sleep', 'health', 'development', 'safety']

  useEffect(() => {
    const fetchTips = async () => {
      try {
        let query = supabase.from('tips').select('*')

        if (selectedCategory !== 'all') {
          query = query.eq('category', selectedCategory)
        }

        const { data, error } = await query.order('category', { ascending: true })

        if (error) throw error
        setTips(data || [])
      } catch (error) {
        console.error('Error fetching tips:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchTips()
  }, [selectedCategory])

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-foreground/70">Loading tips...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-4xl font-bold text-foreground mb-2 flex items-center gap-2">
          <BookOpen className="w-10 h-10 text-primary" />
          Expert Tips
        </h1>
        <p className="text-foreground/70">Curated advice for every stage of your baby&apos;s development</p>
      </div>

      {/* Category Filter */}
      <div className="flex gap-2 flex-wrap">
        <button
          onClick={() => setSelectedCategory('all')}
          className={`px-4 py-2 rounded-lg font-medium transition ${
            selectedCategory === 'all'
              ? 'bg-primary text-primary-foreground'
              : 'bg-white border border-foreground/10 text-foreground hover:border-primary'
          }`}
        >
          All Tips
        </button>
        {categories.map((category) => (
          <button
            key={category}
            onClick={() => setSelectedCategory(category)}
            className={`px-4 py-2 rounded-lg font-medium transition capitalize ${
              selectedCategory === category
                ? 'bg-primary text-primary-foreground'
                : 'bg-white border border-foreground/10 text-foreground hover:border-primary'
            }`}
          >
            {category}
          </button>
        ))}
      </div>

      {/* Tips Grid */}
      {tips.length === 0 ? (
        <div className="text-center py-12 bg-white rounded-lg border border-foreground/10">
          <p className="text-foreground/70">No tips available for this category</p>
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {tips.map((tip) => (
            <div
              key={tip.id}
              className="bg-white rounded-lg shadow-sm border border-foreground/10 p-6 hover:shadow-md transition"
            >
              <div className="flex items-start justify-between mb-3">
                <h3 className="text-lg font-semibold text-foreground flex-1">{tip.title}</h3>
                <span className="ml-2 px-3 py-1 bg-primary/10 text-primary text-xs font-medium rounded-full capitalize">
                  {tip.category}
                </span>
              </div>

              <p className="text-foreground/70 mb-4 leading-relaxed">{tip.content}</p>

              {tip.age_range_months_min !== null || tip.age_range_months_max !== null ? (
                <div className="text-sm text-foreground/60 border-t border-foreground/10 pt-3">
                  <p>
                    <span className="font-medium">Age Range:</span>{' '}
                    {tip.age_range_months_min ?? '0'} - {tip.age_range_months_max ?? 'any'} months
                  </p>
                </div>
              ) : null}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
