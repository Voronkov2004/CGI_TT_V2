<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import FlightListItem from '@/components/FlightListItem.vue'

let departure = ref('');
let arrival = ref('');
let numberOfPeople = ref('');
let currentLocation = ref('');
let destination = ref('');
let outputData = ref([]);
let loading = ref(true);

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/flight/all');
    outputData.value = response.data;
    loading.value = false;
  } catch (error) {
    console.error('Cannot parse data: ', error);
    loading.value = false;
  }
});

const handleSubmit = async (e) => {
  e.preventDefault();
  loading.value = true;

  const isLocationFilled = currentLocation.value.trim() && destination.value.trim();
  const isDatesFilled = departure.value && arrival.value;
  const isPeopleFilled = numberOfPeople.value && parseInt(numberOfPeople.value) > 0;

  try {
    let response;

    if (isLocationFilled && isDatesFilled && isPeopleFilled) {
      response = await axios.get('http://localhost:8080/flight/locationdatepeople', {
        params: {
          current: currentLocation.value,
          destination: destination.value,
          departure: departure.value,
          arrival: arrival.value,
          people: numberOfPeople.value,
        },
      });
    } else if (isLocationFilled && isDatesFilled) {
      response = await axios.get('http://localhost:8080/flight/locationdate', {
        params: {
          current: currentLocation.value,
          destination: destination.value,
          departure: departure.value,
          arrival: arrival.value,
        },
      });
    } else if (isLocationFilled) {
      response = await axios.get('http://localhost:8080/flight/location', {
        params: {
          current: currentLocation.value,
          destination: destination.value,
        },
      });
    } else if (isDatesFilled) {
      response = await axios.get('http://localhost:8080/flight/date', {
        params: {
          departure: departure.value,
          arrival: arrival.value,
        },
      });
    } else {
      response = await axios.get('http://localhost:8080/flight/all');
    }

    console.log('Search results:', response.data);
    outputData.value = response.data;
    loading.value = false;
  } catch (error) {
    console.error('Error fetching data:', error);
    loading.value = false;
  }
};
</script>

<template>
  <form @submit="handleSubmit">
    <div class="form-group">
      <div class="input-container">
        <label for="currentLocation">Current Location</label>
        <input type="text" v-model="currentLocation" name="currentLocation" id="currentLocation">
      </div>

      <div class="input-container">
        <label for="destination">Destination</label>
        <input type="text" v-model="destination" name="destination" id="destination">
      </div>

      <div class="input-container">
        <label for="departure">Departure</label>
        <input type="date" v-model="departure" name="departure" id="departure">
      </div>

      <div class="input-container">
        <label for="arrival">Arrival</label>
        <input type="date" v-model="arrival" name="arrival" id="arrival">
      </div>

      <div class="input-container">
        <label for="numberOfPeople">Number of People</label>
        <input type="number" v-model="numberOfPeople" name="numberOfPeople" id="numberOfPeople">
      </div>
    </div>

    <button type="submit">Search Flights</button>
  </form>

  <div v-if="loading" class="loading">
    Loading flights...
  </div>

  <div v-else>
    <div v-if="outputData && outputData.length">
      <FlightListItem
        v-for="flight in outputData"
        :key="flight.id"
        :flight="flight"
        :group-size="numberOfPeople"
      />
    </div>
    <h2 v-else>No flights found</h2>
  </div>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 200%;
}

.form-group {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  flex-direction: row;
  justify-content: space-between;
}

h2{
  color: red;
}

.loading {
  margin-top: 20px;
  font-style: italic;
  color: #666;
}

.input-container {
  display: flex;
  flex-direction: column;
  width: 15%;
}

label {
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 10px;
  border: none;
  border-radius: 4px;
  background-color: #4CAF50;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>