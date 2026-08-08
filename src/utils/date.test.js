import { describe, expect, it } from 'vitest'
import { toDateInputValue, toEndDateTime, toStartDateTime } from './date'

describe('date helpers', () => {
  it('formats Date to yyyy-MM-dd', () => {
    const date = new Date(2026, 7, 8) // month is 0-based, so 7 = August
    expect(toDateInputValue(date)).toBe('2026-08-08')
  })

  it('builds start and end datetime for backend', () => {
    expect(toStartDateTime('2026-06-01')).toBe('2026-06-01T00:00:00')
    expect(toEndDateTime('2026-08-08')).toBe('2026-08-08T23:59:59')
  })

  it('returns null when date input empty', () => {
    expect(toStartDateTime('')).toBeNull()
    expect(toEndDateTime('')).toBeNull()
  })
})
