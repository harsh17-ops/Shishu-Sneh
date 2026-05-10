import Link from 'next/link'
import { Heart, TrendingUp, Activity, BookOpen } from 'lucide-react'

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <Heart className="w-8 h-8 text-primary" />
            <h1 className="text-2xl font-bold text-primary">Shishu Sneh</h1>
          </div>
          <div className="flex gap-4">
            <Link
              href="/signin"
              className="px-4 py-2 text-foreground hover:text-primary transition"
            >
              Sign In
            </Link>
            <Link
              href="/signup"
              className="px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
            >
              Sign Up
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center mb-16">
          <h2 className="text-5xl font-bold text-foreground mb-4">
            Track Your Baby&apos;s Health Journey
          </h2>
          <p className="text-xl text-foreground/70 mb-8">
            Monitor vaccinations, weight, milestones, and access expert tips for your baby&apos;s growth and development
          </p>
          <Link
            href="/signup"
            className="inline-block px-8 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:opacity-90 transition"
          >
            Get Started Today
          </Link>
        </div>

        {/* Features Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6 mt-16">
          <div className="bg-white rounded-lg p-6 shadow-md hover:shadow-lg transition">
            <Activity className="w-12 h-12 text-primary mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">Vaccination Tracking</h3>
            <p className="text-foreground/70">
              Keep track of scheduled and administered vaccinations with automatic reminders
            </p>
          </div>

          <div className="bg-white rounded-lg p-6 shadow-md hover:shadow-lg transition">
            <TrendingUp className="w-12 h-12 text-primary mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">Growth Monitoring</h3>
            <p className="text-foreground/70">
              Track weight and height with visual charts to monitor healthy development
            </p>
          </div>

          <div className="bg-white rounded-lg p-6 shadow-md hover:shadow-lg transition">
            <BookOpen className="w-12 h-12 text-primary mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">Expert Tips</h3>
            <p className="text-foreground/70">
              Access curated tips on feeding, sleep, health, and development for each stage
            </p>
          </div>

          <div className="bg-white rounded-lg p-6 shadow-md hover:shadow-lg transition">
            <Heart className="w-12 h-12 text-primary mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">Milestone Tracking</h3>
            <p className="text-foreground/70">
              Record and celebrate your baby&apos;s important developmental milestones
            </p>
          </div>
        </div>

        {/* Stats Section */}
        <div className="grid md:grid-cols-3 gap-8 mt-20 text-center">
          <div>
            <p className="text-4xl font-bold text-primary mb-2">1000+</p>
            <p className="text-foreground/70">Parents Using Shishu Sneh</p>
          </div>
          <div>
            <p className="text-4xl font-bold text-primary mb-2">50+</p>
            <p className="text-foreground/70">Expert Tips Available</p>
          </div>
          <div>
            <p className="text-4xl font-bold text-primary mb-2">100%</p>
            <p className="text-foreground/70">Secure & Private</p>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-white border-t border-foreground/10 mt-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 text-center text-foreground/60">
          <p>&copy; 2024 Shishu Sneh. All rights reserved.</p>
        </div>
      </footer>
    </div>
  )
}
