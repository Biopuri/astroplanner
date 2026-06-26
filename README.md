# Astroplanner

Astroplanner is an open-source Java application for planning astronomical observations.

The goal of the project is to help amateur astronomers find the best observation windows for celestial objects based on astronomical calculations and, in the future, weather conditions.

## Features

### Implemented

* Accurate ephemeris calculations powered by Orekit
* Horizontal coordinates (Altitude / Azimuth)
* Observation window search
* Configurable observation criteria:

    * altitude range
    * azimuth range
    * search period
    * search step
* Command-line interface (CLI)

### Planned

* Weather forecast integration
* Cloud coverage analysis
* Moon phase and illumination
* Visibility score
* Export to CSV / PDF
* Desktop UI

---

## Example СLI

Input:

```text
Object: Moon
Latitude: 32.70427
Longitude: 51.17886

Altitude:
10° – 50°

Azimuth:
30° – 74°

Period:
2026-08-25 – 2026-09-25

Time zone:
Asia/Tehran
```

Output:

```text
2026-09-02 22:18 — 22:52
Altitude: 10.30° → 16.92°
Azimuth : 70.07° → 73.85°
```

---

## Project Structure

```
astroplanner
├── astroplanner-core         # Domain model and search algorithms
├── astroplanner-ephemeris    # Orekit integration
├── astroplanner-cli          # Command-line interface
└── astroplanner-desktop      # Desktop app
```

---

## Technology Stack

* Java 21
* Maven
* Orekit

---

## Roadmap

* [x] Project architecture

* [x] Domain model

* [x] Orekit integration

* [x] Horizontal coordinate calculation

* [x] Observation window search

* [x] Interactive CLI

* [x] Desktop application

* [x] Sky illumination mode

* [x] Observer elevation

* [x] Min window duration

* [ ] Weather integration

* [ ] Seeing and transparency

* [ ] Moon phase

