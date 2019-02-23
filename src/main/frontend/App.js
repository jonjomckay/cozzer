import React, { Component } from 'react';
import Header from "./Header";
import { BrowserRouter as Router, Route } from "react-router-dom";
import ProjectList from "./ProjectList";
import Container from "react-bootstrap/Container";
import Project from "./Project";
import TestSuite from "./TestSuite";

class App extends Component {
    render() {
        return (
            <Router>
                <div className="App">
                    <Header />

                    <Container>
                        <Route exact path="/projects" component={ ProjectList } />
                        <Route exact path="/projects/:id" component={ Project } />
                        <Route exact path="/projects/:id/tests/suites/:suite" component={ TestSuite } />
                    </Container>
                </div>
            </Router>
        );
    }
}

export default App;
