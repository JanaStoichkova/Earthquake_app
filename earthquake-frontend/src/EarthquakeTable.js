import React from "react";

export default function EarthquakeTable({ earthquakes, getMagClass, onDelete }) {
  if (!earthquakes || earthquakes.length === 0) {
    return (
      <div className="no-data">
        <h3>No Earthquakes Found</h3>
        <p>Try adjusting your filters or click "Refresh Data"</p>
      </div>
    );
  }

  return (
    <div className="table-container">
      <table>
        <thead>
          <tr>
            <th>Magnitude</th>
            <th>Location</th>
            <th>Time</th>
            <th>Coordinates</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {earthquakes.map((eq) => (
            <tr key={eq.id}>
              <td>
                <span className={`magnitude ${getMagClass(eq.mag)}`}>
                  {eq.mag?.toFixed(1)}
                </span>
              </td>
              <td>{eq.place}</td>
              <td>{new Date(eq.time).toLocaleString()}</td>
              <td>
                {eq.latitude?.toFixed(4)}, {eq.longitude?.toFixed(4)}
              </td>
              <td>
                <button
                  className="delete-btn"
                  onClick={() => onDelete(eq.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
