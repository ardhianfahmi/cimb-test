export function formatCallDate(timestamp) {
    return new Date(timestamp).toLocaleString('id-ID', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });
}

export function sentimentBadgeClass(score) {
    if (score >= 70) return 'badge-success';
    if (score < 70 && score >= 40) return 'badge-warning';
    if (score < 40) return 'badge-error';
}

export function sentimentLabel(score) {
    if (score >= 70) return 'Positive';
    if (score >= 40) return 'Neutral';
    return 'Negative';
}
