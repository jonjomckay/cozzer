import React from 'react';
import Navbar from "react-bootstrap/Navbar";
import NavbarBrand from "react-bootstrap/NavbarBrand";
import NavbarToggle from "react-bootstrap/NavbarToggle";
import NavbarCollapse from "react-bootstrap/NavbarCollapse";
import Nav from "react-bootstrap/Nav";
import NavLink from "react-bootstrap/NavLink";
import { Link } from "react-router-dom";

const Header = () => {
    return (
        <Navbar expand="lg" sticky="top" style={{ marginBottom: '1rem'}}>
            <NavbarBrand>Testmate</NavbarBrand>
            <NavbarToggle aria-controls="navigation-main" />

            <NavbarCollapse id="navigation-main">
                <Nav className="mr-auto">
                    <NavLink as={ Link } to="/">Home</NavLink>
                    <NavLink as={ Link } to="/projects">Projects</NavLink>
                </Nav>
            </NavbarCollapse>
        </Navbar>
    )
};

export default Header;
