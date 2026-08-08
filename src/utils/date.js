// helper for date period (latest 3 months rule)

export function getTodayDateInput() {
  return toDateInputValue(new Date())
}

export function getMinPeriodDateInput() {
  const date = new Date()
  date.setMonth(date.getMonth() - 3)
  return toDateInputValue(date)
}

// convert Date -> yyyy-MM-dd (for <input type="date">)
export function toDateInputValue(date) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

// convert yyyy-MM-dd -> LocalDateTime string for backend
export function toStartDateTime(dateInput) {
  if (!dateInput) return null
  return `${dateInput}T00:00:00`
}

export function toEndDateTime(dateInput) {
  if (!dateInput) return null
  return `${dateInput}T23:59:59`
}

// format timestamp for table display
export function formatTimestamp(value) {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value

  return date.toLocaleString('id-ID', {
    year: 'numeric',
    month: 'short',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
}
