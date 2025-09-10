import { useState, useEffect } from "react";
import { Button, Form, Alert, Card } from "react-bootstrap";
import Select from "react-select";
import { authApis, endpoints } from "../configs/Apis";

const majorOptions = [
    { value: "IT", label: "Công nghệ Thông tin" },
    { value: "LOGISTICS", label: "Logistics" },
    { value: "ACCOUNTING", label: "Kế toán" },
    { value: "AUDIT", label: "Kiểm toán" },
    { value: "COMPUTER_SCIENCE", label: "Khoa học Máy tính" },
    { value: "LAW", label: "Luật" },
    { value: "MARKETING", label: "Marketing" },
    { value: "MEDICINE", label: "Y khoa" },
    { value: "NURSING", label: "Điều dưỡng" },
    { value: "INTERIOR_DESIGN", label: "Thiết kế Nội thất" }
];

const CreateThesis = () => {
    const [thesis, setThesis] = useState({
        name: "",
        students: [],
        lecturers: [],
        major: ""
    });
    const [students, setStudents] = useState([]);
    const [lecturers, setLecturers] = useState([]);
    const [msg, setMsg] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchUsersByRole = async (role, setter) => {
            try {
                const res = await authApis().get(endpoints['users-list'], {
                    params: { role }
                });
                setter(res.data);
            } catch (err) {
                setError("Lỗi khi lấy dữ liệu " + (role === "STUDENT" ? "sinh viên" : "giảng viên"));
            }
        };

        fetchUsersByRole("STUDENT", setStudents);
        fetchUsersByRole("LECTURER", setLecturers);
    }, []);

    const studentOptions = students.map(s => ({
        value: s.id,
        label: `${s.firstName} ${s.lastName}`
    }));

    const lecturerOptions = lecturers.map(l => ({
        value: l.id,
        label: `${l.firstName} ${l.lastName}`
    }));

    const handleSelectChange = (name, selected) => {
        setThesis({ ...thesis, [name]: selected ? selected.map(option => option.value) : [] });
    };

    const handleMajorChange = (selected) => {
        setThesis({ ...thesis, major: selected ? selected.value : "" });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setThesis({ ...thesis, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMsg(null); 
        setError(null);
        setLoading(true);

        try {
            const res = await authApis().post(endpoints['theses'], {
                title: thesis.name,
                studentIds: thesis.students,
                lecturerIds: thesis.lecturers,
                major: thesis.major
            });
            if (res.status === 200) {
                setMsg("Khoá luận đã được tạo thành công!");
                setThesis({ name: "", students: [], lecturers: [], major: "" });
            }
        } catch (err) {
            setError(err.response?.data || "Lỗi khi tạo khóa luận.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4">
            <Card className="shadow-sm">
                <Card.Body>
                    <h2 className="text-primary mb-4">Tạo Khoá Luận Mới</h2>
                    
                    {msg && <Alert variant="success" dismissible onClose={() => setMsg(null)}>{msg}</Alert>}
                    {error && <Alert variant="danger" dismissible onClose={() => setError(null)}>{error}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên khoá luận</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={thesis.name}
                                onChange={handleChange}
                                required
                                placeholder="Nhập tên khoá luận"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Ngành</Form.Label>
                            <Select
                                name="major"
                                options={majorOptions}
                                value={majorOptions.find(opt => opt.value === thesis.major)}
                                onChange={handleMajorChange}
                                placeholder="Chọn ngành..."
                                classNamePrefix="select"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Sinh viên thực hiện</Form.Label>
                            <Select
                                isMulti
                                name="students"
                                options={studentOptions}
                                value={studentOptions.filter(opt => thesis.students.includes(opt.value))}
                                onChange={selected => handleSelectChange("students", selected)}
                                placeholder="Chọn sinh viên..."
                                classNamePrefix="select"
                            />
                        </Form.Group>

                        <Form.Group className="mb-4">
                            <Form.Label>Giảng viên hướng dẫn</Form.Label>
                            <Select
                                isMulti
                                name="lecturers"
                                options={lecturerOptions}
                                value={lecturerOptions.filter(opt => thesis.lecturers.includes(opt.value))}
                                onChange={selected => handleSelectChange("lecturers", selected)}
                                placeholder="Chọn giảng viên..."
                                classNamePrefix="select"
                            />
                        </Form.Group>

                        <div className="text-center">
                            <Button 
                                type="submit" 
                                variant="primary" 
                                size="lg"
                                disabled={loading}
                            >
                                {loading ? "Đang xử lý..." : "Tạo khoá luận"}
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
};

export default CreateThesis;
