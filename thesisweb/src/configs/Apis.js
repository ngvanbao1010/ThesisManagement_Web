// src/configs/Apis.js
import axios from "axios";
import cookie from "react-cookies";

const BASE_URL = "http://localhost:8080/ThesisApp/api";

export const endpoints = {
  'login': "/login",
  'profile': "/secure/profile",
  "change-password": "/secure/change-password",
  "users-list": "/secure/users",
  "create-user": "/register",
  'theses': "/secure/theses",
  "my-thesis": "/secure/theses/mine",
  'committees': "/secure/committees",
  "lock-committee": (id) => `/secure/committees/lock/${id}`,
  "committees-list": "/secure/committees",
  "criteria": "/secure/criteria",
  "evaluation-detail": "/secure/detail",
  "thesis-criteria": (id) => `/secure/thesis/${id}/criteria`,
  "average-score": (id) => `/secure/thesis/${id}/average-score`,
  "lecturers-allowed": "/secure/lecturers/allowed",
  "student-submit": "/student/thesis/file", 
};

export const authApis = () =>
  axios.create({
    baseURL: BASE_URL,
    headers: { Authorization: `Bearer ${cookie.load("token")}` },
  });

export default axios.create({ baseURL: BASE_URL });
