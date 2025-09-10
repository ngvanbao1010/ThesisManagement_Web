import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AdminCreateUser from "./components/CreateUser";
import ChangePassword from "./components/ChangePassword";
import Login from "./components/Login";
import { MyUserContext } from "./configs/Contexts";
import MyUserReducer from "./reducers/MyUserReducer";
import { useReducer } from "react";
import AdminDashboard from "./components/AdminDashboard";
import LecturerHome from "./components/pages/LecturerHome";
import StudentHome from "./components/pages/StudentHome";
import AcademicStaffHome from "./components/pages/AcademicStaffHome";
import UserList from "./components/UserList";
import CreateThesis from "./components/CreateThesis";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Profile from "./components/Profile";
import CreateCommittee from "./components/CreateCommittee";
import CriteriaSetup from "./components/CriteriaSetup";
import LockCommittee from "./components/LockCommittee";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null); 

  return (
    <MyUserContext.Provider value={[user, dispatch]}>
         <div style={{
                minHeight: "100vh",
                display: "flex",
                flexDirection: "column"
            }}>
      <BrowserRouter>
        <Header />
        <div style={{ flex: 1, display: "flex", flexDirection: "column" }}>
        <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/create-user" element={<AdminCreateUser />} />
        <Route path="/change-password" element={<ChangePassword />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin/users" element={<UserList />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/academic_staff" element={<AcademicStaffHome />} />
        <Route path="/lecturer" element={<LecturerHome />} />
        <Route path="/student" element={<StudentHome />} />
        <Route path="/theses" element={<CreateThesis />} />
        <Route path="/committees" element={<CreateCommittee />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/academic_staff/criteria" element={<CriteriaSetup currentUser={user} />} />
        <Route path="/academic_staff/lock-committee" element={<LockCommittee currentUser={user} />} />
        </Routes>
        </div>
      <Footer /> 
      </BrowserRouter>
      </div>
    </MyUserContext.Provider>
  );
};
export default App;