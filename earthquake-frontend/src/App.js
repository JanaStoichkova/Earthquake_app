import React, { useEffect, useState } from "react";
import EarthquakeTable from "./EarthquakeTable";
import EarthquakeMap from "./EarthquakeMap";
import { getEarthquakes, getEarthquakeByMag, getEarthquakeByTime, deleteEarthquake } from "./api";
import "./App.css";

export default function App() {
  const [earthquakes, setEarthquakes] = useState([]);
  const [magFilter, setMagFilter] = useState("");
  const [timeFilterDisplay, setTimeFilterDisplay] = useState("");
  const [timeFilterApi, setTimeFilterApi] = useState("");
  const [activeTab, setActiveTab] = useState("table");

  const loadAll = () => getEarthquakes().then(setEarthquakes);

  const handleDelete = (id) => {
    deleteEarthquake(id).then(() => loadAll());
  };

  const handleMagChange = (e) => {
    const val = e.target.value;
    setMagFilter(val);
    if (val) getEarthquakeByMag(parseFloat(val)).then(setEarthquakes);
    else loadAll();
  };

  const handleTimeChange = (e) => {
    const val = e.target.value;
    setTimeFilterDisplay(val);
    if (!val) {
      setTimeFilterApi("");
      loadAll();
      return;
    }
    const localDate = new Date(val);
    const utcDate = localDate.toISOString();
    setTimeFilterApi(utcDate);
    getEarthquakeByTime(utcDate).then(setEarthquakes);
  };

  useEffect(() => {
    loadAll();
  }, []);

  const getMaxMag = () => {
    if (earthquakes.length === 0) return 0;
    return Math.max(...earthquakes.map((e) => e.mag));
  };

  const getAvgMag = () => {
    if (earthquakes.length === 0) return 0;
    const sum = earthquakes.reduce((acc, e) => acc + e.mag, 0);
    return (sum / earthquakes.length).toFixed(1);
  };

  const getMagClass = (mag) => {
    if (mag >= 3) return "mag-high";
    if (mag >= 2) return "mag-medium";
    return "mag-low";
  };

  return (
    <div className="app">
      <header className="header">
        <h1>Earthquake Dashboard</h1>
        <p>Real-time seismic activity from USGS</p>
      </header>

      <div className="stats-bar">
        <div className="stat-card">
          <h3>{earthquakes.length}</h3>
          <p>Total Earthquakes</p>
        </div>
        <div className="stat-card">
          <h3>{getMaxMag()}</h3>
          <p>Maximum Magnitude</p>
        </div>
        <div className="stat-card">
          <h3>{getAvgMag()}</h3>
          <p>Average Magnitude</p>
        </div>
      </div>

      <div className="controls">
        <h2>Filters</h2>
        <div className="filter-group">
          <div className="filter-item">
            <label>Minimum Magnitude</label>
            <select value={magFilter} onChange={handleMagChange}>
              <option value="">All</option>
              <option value="1.0">≥ 1.0</option>
              <option value="2.0">≥ 2.0</option>
              <option value="3.0">≥ 3.0</option>
            </select>
          </div>
          <div className="filter-item">
            <label>After Time</label>
            <input
              type="datetime-local"
              value={timeFilterDisplay}
              onChange={handleTimeChange}
            />
          </div>
        </div>
        <div className="btn-group">
          <button className="btn btn-primary" onClick={loadAll}>
            Refresh Data
          </button>
          <button className="btn btn-secondary" onClick={() => {
            setMagFilter("");
            setTimeFilterDisplay("");
            setTimeFilterApi("");
            loadAll();
          }}>
            Reset Filters
          </button>
        </div>
      </div>

      <div className="content-section">
        <div className="tabs">
          <button
            className={`tab-btn ${activeTab === "table" ? "active" : ""}`}
            onClick={() => setActiveTab("table")}
          >
            Table View
          </button>
          <button
            className={`tab-btn ${activeTab === "map" ? "active" : ""}`}
            onClick={() => setActiveTab("map")}
          >
            Map View
          </button>
        </div>
        <div className="tab-content">
          {activeTab === "table" ? (
            <EarthquakeTable earthquakes={earthquakes} getMagClass={getMagClass} onDelete={handleDelete} />
          ) : (
            <EarthquakeMap earthquakes={earthquakes} />
          )}
        </div>
      </div>
    </div>
  );
}
