-- Create users table (extends Supabase auth.users)
CREATE TABLE IF NOT EXISTS public.users (
  id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL PRIMARY KEY,
  email TEXT NOT NULL,
  full_name TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW()),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

-- Create babies table
CREATE TABLE IF NOT EXISTS public.babies (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES public.users(id) ON DELETE CASCADE NOT NULL,
  name TEXT NOT NULL,
  date_of_birth DATE NOT NULL,
  gender TEXT CHECK (gender IN ('male', 'female', 'other')),
  blood_type TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW()),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

-- Create vaccinations table
CREATE TABLE IF NOT EXISTS public.vaccinations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  baby_id UUID REFERENCES public.babies(id) ON DELETE CASCADE NOT NULL,
  vaccine_name TEXT NOT NULL,
  scheduled_date DATE NOT NULL,
  administered_date DATE,
  status TEXT CHECK (status IN ('pending', 'administered', 'missed')) DEFAULT 'pending',
  notes TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW()),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

-- Create weight entries table
CREATE TABLE IF NOT EXISTS public.weight_entries (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  baby_id UUID REFERENCES public.babies(id) ON DELETE CASCADE NOT NULL,
  weight_kg DECIMAL(5, 2) NOT NULL,
  recorded_date DATE NOT NULL,
  notes TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW()),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

-- Create milestones table
CREATE TABLE IF NOT EXISTS public.milestones (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  baby_id UUID REFERENCES public.babies(id) ON DELETE CASCADE NOT NULL,
  milestone_name TEXT NOT NULL,
  milestone_age_months INTEGER NOT NULL,
  achieved BOOLEAN DEFAULT FALSE,
  achieved_date DATE,
  notes TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW()),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

-- Create tips table
CREATE TABLE IF NOT EXISTS public.tips (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  category TEXT NOT NULL CHECK (category IN ('feeding', 'sleep', 'health', 'development', 'safety')),
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  age_range_months_min INTEGER,
  age_range_months_max INTEGER,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

-- Enable RLS (Row Level Security)
ALTER TABLE public.users ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.babies ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.vaccinations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.weight_entries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.milestones ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tips ENABLE ROW LEVEL SECURITY;

-- RLS Policies for users table
CREATE POLICY "Users can view their own data" ON public.users
  FOR SELECT USING (auth.uid() = id);

CREATE POLICY "Users can update their own data" ON public.users
  FOR UPDATE USING (auth.uid() = id);

-- RLS Policies for babies table
CREATE POLICY "Users can view their own babies" ON public.babies
  FOR SELECT USING (user_id = auth.uid());

CREATE POLICY "Users can insert babies" ON public.babies
  FOR INSERT WITH CHECK (user_id = auth.uid());

CREATE POLICY "Users can update their own babies" ON public.babies
  FOR UPDATE USING (user_id = auth.uid());

CREATE POLICY "Users can delete their own babies" ON public.babies
  FOR DELETE USING (user_id = auth.uid());

-- RLS Policies for vaccinations table
CREATE POLICY "Users can view vaccinations for their babies" ON public.vaccinations
  FOR SELECT USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can insert vaccinations for their babies" ON public.vaccinations
  FOR INSERT WITH CHECK (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can update vaccinations for their babies" ON public.vaccinations
  FOR UPDATE USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can delete vaccinations for their babies" ON public.vaccinations
  FOR DELETE USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

-- RLS Policies for weight entries table
CREATE POLICY "Users can view weight entries for their babies" ON public.weight_entries
  FOR SELECT USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can insert weight entries for their babies" ON public.weight_entries
  FOR INSERT WITH CHECK (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can update weight entries for their babies" ON public.weight_entries
  FOR UPDATE USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can delete weight entries for their babies" ON public.weight_entries
  FOR DELETE USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

-- RLS Policies for milestones table
CREATE POLICY "Users can view milestones for their babies" ON public.milestones
  FOR SELECT USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can insert milestones for their babies" ON public.milestones
  FOR INSERT WITH CHECK (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can update milestones for their babies" ON public.milestones
  FOR UPDATE USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

CREATE POLICY "Users can delete milestones for their babies" ON public.milestones
  FOR DELETE USING (baby_id IN (SELECT id FROM public.babies WHERE user_id = auth.uid()));

-- RLS Policies for tips table (public, everyone can read)
CREATE POLICY "Tips are public" ON public.tips
  FOR SELECT USING (true);

-- Create indexes for better performance
CREATE INDEX idx_babies_user_id ON public.babies(user_id);
CREATE INDEX idx_vaccinations_baby_id ON public.vaccinations(baby_id);
CREATE INDEX idx_weight_entries_baby_id ON public.weight_entries(baby_id);
CREATE INDEX idx_milestones_baby_id ON public.milestones(baby_id);
CREATE INDEX idx_tips_category ON public.tips(category);
