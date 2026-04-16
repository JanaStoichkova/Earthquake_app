import React from "react";
import { MapContainer, TileLayer, CircleMarker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";

export default function EarthquakeMap({ earthquakes }) {
  if (!earthquakes || earthquakes.length === 0) {
    return (
      <div className="no-data">
        <h3>No Earthquakes to Display</h3>
        <p>Try adjusting your filters or click "Refresh Data"</p>
      </div>
    );
  }

  const getColor = (mag) => {
    if (mag >= 3) return "#c62828";
    if (mag >= 2) return "#ef6c00";
    return "#2e7d32";
  };

  return (
    <div className="map-wrapper">
      <MapContainer
        center={[20, 0]}
        zoom={2}
        style={{ width: "100%", height: "500px" }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {earthquakes.map((eq) => (
          <CircleMarker
            key={eq.id}
            center={[eq.latitude, eq.longitude]}
            radius={Math.max(eq.mag * 2, 5)}
            pathOptions={{
              color: getColor(eq.mag),
              fillColor: getColor(eq.mag),
              fillOpacity: 0.7,
              weight: 1,
            }}
          >
            <Popup>
              <strong>{eq.place}</strong>
              <br />
              Magnitude: <span style={{ color: getColor(eq.mag) }}>{eq.mag?.toFixed(1)}</span>
              <br />
              Time: {new Date(eq.time).toLocaleString()}
            </Popup>
          </CircleMarker>
        ))}
      </MapContainer>
      <div className="map-legend">
        <div className="legend-item">
          <span className="legend-dot" style={{ background: "#2e7d32" }}></span>
          <span>&lt; 2.0</span>
        </div>
        <div className="legend-item">
          <span className="legend-dot" style={{ background: "#ef6c00" }}></span>
          <span>2.0 - 2.9</span>
        </div>
        <div className="legend-item">
          <span className="legend-dot" style={{ background: "#c62828" }}></span>
          <span>3.0+</span>
        </div>
      </div>
    </div>
  );
}
