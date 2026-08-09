export function getUserInitials(fullName = '') {
    const parts = fullName.trim().split(/\s+/).filter(Boolean);

    if (parts.length >= 2) {
        return `${parts[0][0]}${parts[1][0]}`.toUpperCase();
    }

    if (parts.length === 1) {
        return parts[0].slice(0, 2).toUpperCase();
    }

    return '?';
}

export function roleBadgeClass(role = '') {
    const normalized = role.toUpperCase();

    if (normalized === 'SUPERVISOR') return 'badge-primary';
    if (normalized === 'ADMIN') return 'badge-secondary';
    if (normalized === 'AGENT') return 'badge-accent';

    return 'badge-neutral';
}
