import { Container, Row, Col } from "react-bootstrap";

const Footer = () => {
    return (
        <footer className="bg-dark text-white py-3 mt-5">
            <Container>
                <Row>
                    <Col className="text-center">
                        <p>© 2025 Hệ thống Quản lý Khóa Luận OU</p>
                        <p>Liên hệ: info@ou.edu.vn</p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
};

export default Footer;
