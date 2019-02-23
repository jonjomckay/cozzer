import React, { Component } from 'react';
import TestSuiteListItem from "./TestSuiteListItem";
import Table from "react-bootstrap/Table";

export default class TestSuiteList extends Component {
    state = {
        suites: []
    };

    componentDidMount() {
        fetch('/api/1/projects/' + this.props.project + '/tests/suites')
            .then(response => response.json())
            .then(response => this.setState({
                suites: response
            }));
    }

    render() {
        const rows = this.state.suites.map(suite => <TestSuiteListItem key={ suite.id } project={ this.props.project } suite={ suite } />);

        return (
            <Table bordered={ false }>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Cases</th>
                </tr>
                </thead>

                <tbody>
                { rows }
                </tbody>
            </Table>
        )
    }
}
