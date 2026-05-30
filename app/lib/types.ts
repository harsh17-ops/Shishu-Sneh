export interface Baby {
  id: string
  user_id: string
  name: string
  date_of_birth: string
  gender?: string
  blood_type?: string
  created_at: string
  updated_at: string
}

export interface Vaccination {
  id: string
  baby_id: string
  vaccine_name: string
  scheduled_date: string
  administered_date?: string
  status: 'pending' | 'administered' | 'missed'
  notes?: string
  created_at: string
  updated_at: string
}

export interface WeightEntry {
  id: string
  baby_id: string
  weight_kg: number
  recorded_date: string
  notes?: string
  created_at: string
  updated_at: string
}

export interface Milestone {
  id: string
  baby_id: string
  milestone_name: string
  milestone_age_months: number
  achieved: boolean
  achieved_date?: string
  notes?: string
  created_at: string
  updated_at: string
}

export interface Tip {
  id: string
  category: 'feeding' | 'sleep' | 'health' | 'development' | 'safety'
  title: string
  content: string
  age_range_months_min?: number
  age_range_months_max?: number
  created_at: string
}

export interface User {
  id: string
  email: string
  full_name?: string
  created_at: string
  updated_at: string
}
