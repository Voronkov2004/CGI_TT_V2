<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import PlaneSeatLayout from '@/components/PlaneSeatLayout.vue'

const route = useRoute();
const flight = ref(null);
const error = ref('');

const groupSize = Number(route.query.groupSize) || 0;

onMounted(async () => {
  try {
    const response = await axios.get(`http://localhost:8080/flight/${route.params.id}`);
    flight.value = response.data;
  } catch (err) {
    error.value = 'Flight not found';
  }
});
</script>

<template>
  <div v-if="flight">
    <h2>Flight Details</h2>
    <p>Aircraft: {{ flight.aircraft }}</p>
    <p>From: {{ flight.currentLocation }}</p>
    <p>To: {{ flight.destination }}</p>
    <p>Departure: {{ flight.departure }} at {{ flight.departureTime }}</p>
    <p>Arrival: {{ flight.arrival }} at {{ flight.arrivalTime }}</p>
    <p>Economy Price: ${{ flight.economyClassPrice }}</p>
    <p>First Class Price: ${{ flight.firstClassPrice }}</p>
    <p>Seats Available: {{ 366 - flight.seatsTaken }}</p>
    <PlaneSeatLayout :flight="flight" :group-size="groupSize"/>
  </div>
  <p v-else>{{ error }}</p>
</template>

<style scoped>
h2 {
  margin-bottom: 15px;
}
</style>
