# Shishu Sneh - Android to Web Conversion

## Overview

The Shishu Sneh Android application has been successfully converted into a full-stack web application built with **Next.js 16**, **TypeScript**, **React**, **Tailwind CSS**, and **Supabase**.

## Tech Stack

### Frontend
- **Framework**: Next.js 16 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **UI Components**: Radix UI, Lucide Icons
- **Data Fetching**: SWR (Stale While Revalidate)
- **State Management**: React Context (built-in) + Local State

### Backend
- **Database**: Supabase PostgreSQL
- **Authentication**: Supabase Auth
- **Row Level Security (RLS)**: Enabled for all tables
- **API**: Supabase Client SDK

### Deployment
- **Hosting**: Vercel (recommended)
- **Environment Variables**: Managed via Vercel Project Settings

## Project Structure

```
shishu-sneh-web/
├── app/
│   ├── (auth)/                           # Authentication pages
│   │   ├── signin/page.tsx
│   │   ├── signup/page.tsx
│   │   └── layout.tsx
│   ├── (dashboard)/                      # Protected dashboard pages
│   │   ├── dashboard/page.tsx            # Home dashboard
│   │   ├── babies/page.tsx               # Baby list
│   │   ├── babies/new/page.tsx           # Add new baby
│   │   ├── babies/[id]/page.tsx          # Baby details (vaccinations, weight, milestones)
│   │   ├── babies/[id]/edit/page.tsx     # Edit baby profile
│   │   ├── babies/[id]/vaccinations/new/page.tsx
│   │   ├── babies/[id]/vaccinations/[vaccineId]/edit/page.tsx
│   │   ├── babies/[id]/weight/new/page.tsx
│   │   ├── babies/[id]/weight/[weightId]/edit/page.tsx
│   │   ├── babies/[id]/milestones/new/page.tsx
│   │   ├── babies/[id]/milestones/[milestoneId]/edit/page.tsx
│   │   ├── tips/page.tsx                 # Educational tips
│   │   ├── layout.tsx                    # Dashboard layout with navigation
│   │
│   ├── lib/
│   │   ├── supabase.ts                   # Supabase client configuration
│   │   └── types.ts                      # TypeScript types
│   ├── page.tsx                          # Landing page
│   ├── layout.tsx                        # Root layout
│   └── globals.css                       # Global styles
├── database.sql                          # Database schema and RLS policies
├── next.config.js
├── tailwind.config.ts
├── tsconfig.json
├── postcss.config.js
└── package.json
```

## Feature Mapping (Android → Web)

| Android Feature | Web Implementation |
|---|---|
| User Registration | `/auth/signup` - Email/password signup with Supabase |
| User Login | `/auth/signin` - Email/password login |
| Baby Profile Management | `/babies` - CRUD operations for baby profiles |
| Vaccination Tracking | `/babies/[id]` - Vaccination tab with add/edit/delete |
| Weight Tracking | `/babies/[id]` - Weight tab with entries and history |
| Milestone Tracking | `/babies/[id]` - Milestone tab for developmental milestones |
| Educational Tips | `/tips` - Filtered tips by category (feeding, sleep, health, etc.) |
| Vaccination Reminders | Dashboard showing upcoming vaccinations within 30 days |
| Dashboard | `/dashboard` - Overview of babies, upcoming vaccines, and stats |

## Database Schema

### Tables
- **users** - Extended auth.users with profile information
- **babies** - Baby profiles with basic info (name, DOB, gender, blood type)
- **vaccinations** - Vaccination records with status tracking
- **weight_entries** - Weight tracking history
- **milestones** - Developmental milestones tracking
- **tips** - Educational tips (public, not user-specific)

All tables (except tips) have Row Level Security enabled so users can only see their own data.

## Getting Started

### Prerequisites
- Node.js 18+ and npm
- Supabase account (create at https://supabase.com)
- Vercel account (for deployment)

### Setup Instructions

1. **Environment Setup**
   - Add the following environment variables in your Vercel project settings:
   ```
   NEXT_PUBLIC_SUPABASE_URL=your_supabase_url
   NEXT_PUBLIC_SUPABASE_ANON_KEY=your_anon_key
   SUPABASE_SERVICE_ROLE_KEY=your_service_role_key
   ```

2. **Database Setup**
   - In your Supabase project, go to SQL Editor
   - Create a new query and paste the contents of `database.sql`
   - Run the query to create all tables and RLS policies

3. **Local Development**
   ```bash
   npm install
   npm run dev
   ```
   - App runs at http://localhost:3000

4. **Build for Production**
   ```bash
   npm run build
   npm start
   ```

5. **Deploy to Vercel**
   ```bash
   # Using Vercel CLI
   vercel deploy
   ```

## Key Features

### Authentication
- Email/password based signup and signin
- Session management with Supabase Auth
- Protected routes (dashboard requires authentication)
- Automatic redirect to signin for unauthenticated users

### Baby Management
- Create multiple baby profiles per user
- Store baby details (name, DOB, gender, blood type)
- Edit baby information anytime
- Delete baby profile (cascades to all related data)

### Vaccination Tracking
- Add vaccinations with scheduled dates
- Track administration status (pending, administered, missed)
- Record date when vaccine was administered
- Add notes for each vaccination
- Automatic upcoming vaccine alerts (next 30 days)
- List of pending vaccinations on dashboard

### Weight Tracking
- Record baby weight with date
- View weight history
- Edit or delete entries
- Foundation for growth charts (can be extended)

### Milestone Tracking
- Track developmental milestones
- Mark milestones as achieved with date
- View all milestones for a baby
- Predefined common milestones with custom option

### Educational Tips
- Browse tips by category:
  - Feeding
  - Sleep
  - Health
  - Development
  - Safety
- Age-range filtering (can be extended)
- Public tips visible to all logged-in users

### Dashboard
- Overview of all babies
- Quick stats (number of babies, upcoming vaccines)
- Alerts for pending vaccinations
- Quick navigation to baby details

## Mobile Responsive Design

The web app is fully responsive and works on:
- Desktop (1920px+)
- Tablet (768px - 1024px)
- Mobile (320px - 767px)

All forms, tables, and layouts adapt to screen size using Tailwind CSS breakpoints.

## Security Features

- **Row Level Security (RLS)**: All user data is protected by database-level RLS policies
- **Authentication**: Supabase Auth with secure session management
- **HTTPS**: Enforced when deployed to Vercel
- **Secure Cookies**: Auth tokens stored in HTTP-only cookies
- **No client-side data storage**: All data persisted in Supabase

## Future Enhancements

Possible features to add:
- Weight/height growth charts with Recharts
- Vaccination schedule by country/region
- Email/SMS reminders for upcoming vaccinations
- Multiple parent/guardian access to same baby profile
- Photo uploads for milestones
- Feeding schedule tracking
- Sleep tracking
- Health records and checkup notes
- Medication tracking
- Pediatrician contact management
- Export data as PDF
- Dark mode theme

## Troubleshooting

### Common Issues

1. **"Failed to execute query" error**
   - Ensure database.sql has been executed in Supabase SQL Editor
   - Check RLS policies are enabled on all tables

2. **Auth not working**
   - Verify NEXT_PUBLIC_SUPABASE_URL and NEXT_PUBLIC_SUPABASE_ANON_KEY are set
   - Check Supabase project settings → Auth

3. **Cannot see data after login**
   - Confirm user_id matches between auth.users and public.users table
   - Check RLS policies allow SELECT for authenticated users

4. **Port 3000 already in use**
   - Use `npm run dev -- -p 3001` for different port

## Development Notes

- All pages use the App Router (modern Next.js pattern)
- Server Components where possible for performance
- Client Components ("use client") only where needed for interactivity
- TypeScript for type safety
- Tailwind CSS for styling (no separate CSS files)
- Supabase for real-time database updates (can be extended)

## Performance Optimization

- Next.js Image Optimization (if adding images)
- CSS purging with Tailwind
- Code splitting with dynamic imports
- SWR for efficient data fetching and caching

## License

Same as original Shishu Sneh project

## Support

For issues or questions about the web conversion, refer to:
- Next.js Documentation: https://nextjs.org/docs
- Supabase Documentation: https://supabase.com/docs
- React Documentation: https://react.dev
- Tailwind CSS Documentation: https://tailwindcss.com
