# Call Monitoring — Frontend

Vue.js frontend for the CIMB Call Monitoring take-home test. Supervisors can sign in, browse call records (search, filter, sort, paginate), switch between table / list / grid views, and manage users.

> Backend API + PostgreSQL are required. This repo is the **frontend** only. Point it at your running Spring Boot (or alternative) API.

---

## Tech Stack

| Area      | Choice                   |
| --------- | ------------------------ |
| Framework | Vue 3 (Composition API)  |
| Build     | Vite 8                   |
| State     | Pinia                    |
| Routing   | Vue Router               |
| HTTP      | Axios                    |
| Styling   | Tailwind CSS 4 + DaisyUI |
| Icons     | Heroicons                |
| Tests     | Vitest                   |

---

## Prerequisites

- Node.js **18+** (recommended: 20+)
- npm (or pnpm / yarn)
- Backend API running (default proxy: `http://localhost:8080`)

---

## Getting Started

### 1. Install dependencies

```bash
npm install
```

### 2. Environment

Create a `.env` file in the project root:

```env
VITE_API_BASE_URL=http://localhost:8080
```

> In local development, Vite also proxies `/api` → `http://localhost:8080` (see `vite.config.js`). You can leave `VITE_API_BASE_URL` empty/`/` if you prefer same-origin requests through the proxy.

### 3. Run the app

```bash
npm run dev
```

Open [http://localhost:5173](http://localhost:5173).

### 4. Production build

```bash
npm run build
npm run preview
```

---

## Tests

```bash
npm test
```

This runs Vitest unit tests (e.g. `src/utils/date.test.js`).

---

## Features

### Auth

- Login / register against backend REST API
- JWT attached via Axios interceptor (`Authorization: Bearer …`)

### Call Monitoring

- List call records from PostgreSQL via backend API (no hardcoded table data)
- Search by CS / customer name
- Filter by sentiment & sort order
- Desktop: table / list / grid + classic pagination (with page size)
- Mobile: list / grid only, defaults to list, **infinite scroll** for loading more pages

### Users

- User list with search
- Table / list / grid views
- Grid avatars use the first two initials from `full_name`
- User `id` is not displayed

### UI

- Responsive layout (desktop shell + mobile-friendly toolbar)
- Light / dark theme toggle
- Loading, empty, and error states

---

## Project Structure

```text
src/
  api/                  # REST clients (call-monitoring, users, auth)
  components/
    base/               # Button, IconButton, FormFieldset, Modal, Pagination, …
    layout/             # AuthShell, DashboardShell, Navbar, Sidebar
  stores/               # Pinia stores (auth, theme, call-monitoring, users)
  views/
    auth/
    dashboard/
      call-monitoring/  # MonitoringList + view components
      users/            # UserList + view components
  routes/
  lib/axios.js
```

---

## Scripts

| Command           | Description                     |
| ----------------- | ------------------------------- |
| `npm run dev`     | Start Vite dev server (`:5173`) |
| `npm run build`   | Production build                |
| `npm run preview` | Preview production build        |
| `npm test`        | Run unit tests (Vitest)         |
| `npm run format`  | Format with Prettier            |

---

## AI Usage

### AI tool used

- **Claude.ai** (Anthropic)

### Parts of the work assisted by AI

- Implementing **infinite scroll on mobile** for the Call Monitoring (and Users) list: IntersectionObserver sentinel, append-on-load-more, hide classic pagination below the `lg` breakpoint, and force list view (hide table toggle) on small screens.

### Example main prompt

```text
When on mobile view, make the data loading using infinite scroll. Follow the code pattern that i was create before.
```

### How results were checked / verified

1. Reviewed generated code against the existing Pinia store + API contract (`page`, `size`, `total_pages`).
2. Manually tested in Chrome DevTools responsive mode (`< 1024px`):
    - starts in list view
    - table toggle hidden
    - scrolling to the bottom loads the next page and appends rows
    - “End of results” appears on the last page
3. Confirmed desktop still uses normal pagination and table/list/grid switcher.
4. Fixed HMR/Pinia edge cases (prefer direct state mutation over newly added actions when needed).
5. Ensured loading-more does not replace the existing list (append-only path).

---

## Notes for Reviewers

- All table data comes from the backend API / PostgreSQL — nothing is hardcoded in the UI.
- Ensure the backend is up before exercising login, call monitoring, and users.
- Default API base / proxy target: `http://localhost:8080`.
