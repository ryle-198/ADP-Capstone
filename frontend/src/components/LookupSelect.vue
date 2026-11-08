<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  options: { type: Array, default: () => [] },
  placeholder: { type: String, default: 'Search...' },
  allowEmpty: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false }
})
const emit = defineEmits(['update:modelValue'])

const filter = ref('')

//the current selection always stays in the list, even if the search text hides it
const filtered = computed(() => {
  const term = filter.value.trim().toLowerCase()
  if (!term) return props.options
  return props.options.filter(o =>
      o.value === props.modelValue || o.label.toLowerCase().includes(term))
})
</script>

<template>
  <div>
    <input v-model="filter" type="text" :placeholder="placeholder" :disabled="disabled" />
    <select :value="modelValue" :disabled="disabled"
            @change="emit('update:modelValue', $event.target.value)">
      <option value="" :disabled="!allowEmpty">{{ allowEmpty ? '-- none --' : '-- select --' }}</option>
      <option v-for="o in filtered" :key="o.value" :value="o.value">{{ o.label }}</option>
    </select>
  </div>
</template>