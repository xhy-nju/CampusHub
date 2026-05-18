<script setup lang="ts">
defineProps<{
  title: string
  description: string
}>()
</script>

<template>
  <main class="placeholder-view">
    <p class="placeholder-view__eyebrow">CampusHub</p>
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>
  </main>
</template>
