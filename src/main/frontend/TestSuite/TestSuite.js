import React, { Component } from 'react';
import axios from 'axios';
import classNames from 'classnames';
import Table from "react-bootstrap/Table";

export default class TestSuite extends Component {
    state = {
        suite: null
    };

    componentDidMount() {
        axios.get('/api/1/projects/' + this.props.match.params.id + '/tests/suites/' + this.props.match.params.suite)
            .then(response => this.setState({
                suite: response.data
            }));
    }

    render() {
        const suite = this.state.suite;

        if (suite === null) {
            return null;
        }

        const cases = this.state.suite.testCases.map(testCase => {
            const nameClasses = classNames({
                'text-danger': testCase.failed || testCase.errored,
                'text-success': testCase.successful,
                'text-muted': testCase.skipped
            });

            const durationClasses = classNames({
                'text-danger': testCase.duration > 10,
                'text-warning': testCase.duration > 5
            });

            return (
                <tr key={ testCase.id }>
                    <td className={ nameClasses }>{ testCase.name }</td>
                    <td className={ durationClasses }>{ testCase.duration }</td>
                    <td>{ testCase.failed ? '✔️' : '' }</td>
                    <td>{ testCase.errored ? '✔️' : '' }</td>
                    <td>{ testCase.skipped ? '✔️' : '' }</td>
                    <td>{ testCase.successful ? '✔️' : '' }</td>
                </tr>
            );
        });

        return (
            <div>
                { suite.name }

                <Table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Duration</th>
                        <th>Failed?</th>
                        <th>Errored?</th>
                        <th>Skipped?</th>
                        <th>Successful?</th>
                    </tr>
                    </thead>

                    <tbody>
                    { cases }
                    </tbody>
                </Table>
            </div>
        )
    }
}
