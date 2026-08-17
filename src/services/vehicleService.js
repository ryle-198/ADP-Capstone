import api from "./api";

export default {
    getAll() {
        return api.get("/vehicle/getAll");
    },

    getById(vehicleId) {
        return api.get(`/vehicle/read/${vehicleId}`);
    },

    create(vehicle) {
        return api.post("/vehicle/create", vehicle);
    },

    update(vehicle) {
        return api.put("/vehicle/update", vehicle);
    },

    delete(vehicleId) {
        return api.delete(`/vehicle/delete/${vehicleId}`);
    }
};