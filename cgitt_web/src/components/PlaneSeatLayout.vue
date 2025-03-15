<script setup>
import { ref, watch } from 'vue';

const totalSeats = 366;
const rows = 61;
const columns = ['A', 'B', 'C', 'D', 'E', 'F'];

const props = defineProps({
  flight: {
    type: Object,
    required: true,
  },
  groupSize: {
    type: Number,
    default: 0,
  },
});

const seats = ref(
  Array.from({ length: rows }, (_, rowIndex) =>
    columns.map(col => ({
      seatNumber: `${rowIndex + 1}${col}`,
      isBooked: false,
      isSelected: false,
      recommended: false,
    }))
  )
);

const toggleSeat = (rowIndex, colIndex) => {
  const seat = seats.value[rowIndex][colIndex];
  if (!seat.isBooked) {
    seat.isSelected = !seat.isSelected;
  }
};

function recommendSeats() {
  if (props.groupSize <= 0) return;

  seats.value.forEach(row => row.forEach(seat => (seat.recommended = false)));

  const middleRowIndex = Math.floor(seats.value.length / 2);

  const rowCheckOrder = [];
  for (let offset = 0; offset < seats.value.length; offset++) {
    if (offset === 0) {
      rowCheckOrder.push(middleRowIndex);
    } else {
      const upperRow = middleRowIndex - offset;
      const lowerRow = middleRowIndex + offset;

      if (upperRow >= 0) rowCheckOrder.push(upperRow);
      if (lowerRow < seats.value.length) rowCheckOrder.push(lowerRow);
    }
  }

  if (props.groupSize <= columns.length) {
    for (const rowIndex of rowCheckOrder) {
      const row = seats.value[rowIndex];

      if (props.groupSize < 3) {
        const sideColumns = [0, 1, 2];

        for (let startCol = 0; startCol <= sideColumns.length - props.groupSize; startCol++) {
          let allAvailable = true;
          for (let i = 0; i < props.groupSize; i++) {
            const colIndex = sideColumns[startCol + i];
            if (row[colIndex].isBooked) {
              allAvailable = false;
              break;
            }
          }

          if (allAvailable) {
            for (let i = 0; i < props.groupSize; i++) {
              row[sideColumns[startCol + i]].recommended = true;
            }
            return;
          }
        }
      } else {
        const middleColIndex = Math.floor(columns.length / 2) - Math.floor(props.groupSize / 2);

        let allAvailable = true;
        for (let i = 0; i < props.groupSize; i++) {
          const colIndex = middleColIndex + i;
          if (colIndex >= columns.length || row[colIndex].isBooked) {
            allAvailable = false;
            break;
          }
        }

        if (allAvailable) {
          for (let i = 0; i < props.groupSize; i++) {
            row[middleColIndex + i].recommended = true;
          }
          return;
        }

        const possibleStartPositions = [];

        for (let start = 0; start <= columns.length - props.groupSize; start++) {
          possibleStartPositions.push({
            start,
            distance: Math.abs(start + props.groupSize/2 - columns.length/2)
          });
        }

        possibleStartPositions.sort((a, b) => a.distance - b.distance);

        for (const position of possibleStartPositions) {
          allAvailable = true;
          for (let i = 0; i < props.groupSize; i++) {
            if (row[position.start + i].isBooked) {
              allAvailable = false;
              break;
            }
          }

          if (allAvailable) {
            for (let i = 0; i < props.groupSize; i++) {
              row[position.start + i].recommended = true;
            }
            return;
          }
        }
      }
    }
  } else {
    let remainingSeats = props.groupSize;
    let recommendedSeats = [];

    for (const rowIndex of rowCheckOrder) {
      const row = seats.value[rowIndex];

      for (let colIndex = 0; colIndex < columns.length; colIndex++) {
        if (!row[colIndex].isBooked) {
          recommendedSeats.push({ rowIndex, colIndex });
          remainingSeats--;

          if (remainingSeats === 0) break;
        }
      }

      if (remainingSeats === 0) break;
    }

    if (remainingSeats === 0) {
      recommendedSeats.forEach(seat => {
        seats.value[seat.rowIndex][seat.colIndex].recommended = true;
      });
      return;
    }
  }
}

watch(
  () => props.flight,
  (newFlight) => {
    if (newFlight) {
      seats.value = Array.from({ length: rows }, (_, rowIndex) =>
        columns.map(col => ({
          seatNumber: `${rowIndex + 1}${col}`,
          isBooked: false,
          isSelected: false,
          recommended: false,
        }))
      );

      if (newFlight.seatsTaken) {
        let seatsToBook = newFlight.seatsTaken;
        const totalSeatCount = rows * columns.length;
        seatsToBook = Math.min(seatsToBook, totalSeatCount);

        let indices = Array.from({ length: totalSeatCount }, (_, i) => i);
        for (let i = indices.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [indices[i], indices[j]] = [indices[j], indices[i]];
        }

        for (let i = 0; i < seatsToBook; i++) {
          const index = indices[i];
          const rowIndex = Math.floor(index / columns.length);
          const colIndex = index % columns.length;
          seats.value[rowIndex][colIndex].isBooked = true;
        }
      }
      recommendSeats();
    }
  },
  { immediate: true }
);

watch(
  () => props.groupSize,
  () => {
    seats.value.forEach(row => row.forEach(seat => (seat.recommended = false)));
    recommendSeats();
  }
);
</script>

<template>
  <div class="plane-container">
    <div class="plane">
      <h2 class="plane-title">Select Your Seats</h2>
      <div class="seat-legend">
        <div class="legend-item">
          <div class="legend-box available"></div>
          <span>Available</span>
        </div>
        <div class="legend-item">
          <div class="legend-box booked"></div>
          <span>Booked</span>
        </div>
        <div class="legend-item">
          <div class="legend-box selected"></div>
          <span>Selected</span>
        </div>
        <div class="legend-item">
          <div class="legend-box recommended"></div>
          <span>Recommended</span>
        </div>
      </div>
      <div class="column-labels">
        <div class="column-label">A</div>
        <div class="column-label">B</div>
        <div class="column-label">C</div>
        <div class="column-label aisle-gap">D</div>
        <div class="column-label">E</div>
        <div class="column-label">F</div>
      </div>
      <div class="seats-container">
        <div class="seats">
          <div v-for="(row, rowIndex) in seats" :key="rowIndex" class="seat-row">
            <div class="row-number">{{ rowIndex + 1 }}</div>
            <div
              v-for="(seat, colIndex) in row"
              :key="seat.seatNumber"
              class="seat"
              :class="{
                booked: seat.isBooked,
                selected: seat.isSelected,
                recommended: seat.recommended,
                'aisle-gap': colIndex === 2
              }"
              @click="toggleSeat(rowIndex, colIndex)"
            >
              {{ seat.seatNumber }}
            </div>
            <div class="row-number">{{ rowIndex + 1 }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.plane-container {
  width: 100%;
  display: flex;
  justify-content: center;
  margin: 2rem 0;
}

.plane {
  padding: 24px;
  max-width: 900px;
  width: 100%;
  margin: 0 auto;
  background-color: gray;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.plane-title {
  margin: 0 0 16px 0;
  font-size: 1.5rem;
  color: #2c3e50;
  text-align: center;
  font-weight: 600;
}

.seat-legend {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.legend-box {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.legend-box.available {
  background-color: white;
}

.legend-box.booked {
  background-color: #ff4d4f;
}

.legend-box.selected {
  background-color: #52c41a;
}

.legend-box.recommended {
  border: 2px solid green;
  background-color: white;
}

.column-labels {
  display: flex;
  justify-content: center;
  margin-bottom: 12px;
  padding: 0 40px;
}

.column-label {
  width: 40px;
  text-align: center;
  font-weight: bold;
  color: #2c3e50;
}

.column-label.aisle-gap {
  margin-left: 15px;
}

.seats-container {
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #e1e4e8;
  border-radius: 6px;
  background: white;
}

.seats {
  display: flex;
  flex-direction: column;
  gap: 5px;
  padding: 10px;
}

.seat-row {
  display: flex;
  justify-content: center;
  gap: 5px;
  align-items: center;
}

.row-number {
  width: 30px;
  text-align: center;
  font-weight: 500;
  color: #2c3e50;
}

.seat {
  width: 40px;
  height: 40px;
  border: 1px solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 6px;
  user-select: none;
  font-size: 0.85rem;
  transition: all 0.2s ease;
  background-color: white;
  color: black;
}

.seat.aisle-gap {
  margin-right: 15px;
}

.seat.booked {
  background-color: #ff4d4f;
  color: white;
  cursor: not-allowed;
  border-color: #ff4d4f;
}

.seat.selected {
  background-color: #52c41a;
  color: white;
  border-color: #52c41a;
}

.seat.recommended {
  border: 2px solid green;
  box-shadow: 0 0 4px rgba(0, 128, 0, 0.3);
}

.seat:not(.booked):hover {
  background-color: #e6f7ff;
  transform: translateY(-2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .seat {
    width: 30px;
    height: 30px;
    font-size: 0.7rem;
  }

  .column-label {
    width: 30px;
  }
}
</style>