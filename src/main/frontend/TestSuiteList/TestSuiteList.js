import React, { Component } from 'react';
import PropTypes from 'prop-types';
import TestSuiteListItem from "./TestSuiteListItem";
import Table from "react-bootstrap/Table";

class TestSuiteList extends Component {
    state = {
        suites: []
    };

    componentDidMount() {
        fetch('/api/1/projects/' + this.props.project + '/submissions/' + this.props.submission + '/tests/suites')
            .then(response => response.json())
            .then(response => this.setState({
                suites: response
            }));
    }

    render() {
        const itemProps = {
            project: this.props.project,
            submission: this.props.submission
        };

        const rows = this.state.suites
            .map(suite => <TestSuiteListItem { ...itemProps } key={ suite.id } suite={ suite } />);

        if (rows.length === 0) {
            return <p>No suites submitted yet!</p>
        }

        return (
            <Table bordered={ false }>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Duration</th>
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

TestSuiteList.propTypes = {
    project: PropTypes.string.isRequired,
    submission: PropTypes.string.isRequired
};

export default TestSuiteList;
