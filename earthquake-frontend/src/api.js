const API_BASE = "http://localhost:8080/api/earthquakes";

export const getEarthquakes = () =>
    fetch(`${API_BASE}`)
        .then(res => res.json());

export const getEarthquakeByMag = (mag) =>
    fetch(`${API_BASE}/filter-mag/${mag}`)
        .then(res => res.json());

export const getEarthquakeByTime = (timeStr) =>
    fetch(`${API_BASE}/search?time=${encodeURIComponent(timeStr)}`)
        .then(res => res.json());

export const deleteEarthquake = (id) =>
    fetch(`${API_BASE}/delete/${id}`, { method: 'DELETE' });