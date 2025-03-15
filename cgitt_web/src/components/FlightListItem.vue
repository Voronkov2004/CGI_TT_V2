<script setup>
defineProps({
  flight: {
    type: Object,
    required: true,
  },
  groupSize: {
    type: Number,
    default: 0,
  },
});
</script>

<template>
  <router-link :to="`/flight/${flight.id}?groupSize=${groupSize}`">
    <div class="flight-card">
      <h2>{{ flight.currentLocation }} -> {{ flight.destination }}</h2>
      <p>Aircraft: {{ flight.aircraft }}</p>
      <p>Departure: {{ flight.departure }} at {{ flight.departureTime }}</p>
      <p>Arrival: {{ flight.arrival }} at {{ flight.arrivalTime }}</p>
      <p>Economy Class Price: ${{ flight.economyClassPrice }}</p>
      <p>First Class Price: ${{ flight.firstClassPrice }}</p>
      <p>Seats Available: {{ 366 - flight.seatsTaken }}</p>
    </div>
  </router-link>
</template>

<style scoped>
.flight-card {
  border: 1px solid #ccc;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>
