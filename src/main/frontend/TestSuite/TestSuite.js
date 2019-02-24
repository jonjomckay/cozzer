import React, { Component } from 'react';
import classNames from 'classnames';
import Table from "react-bootstrap/Table";

export default class TestSuite extends Component {
    state = {
        suite: null
    };

    componentDidMount() {
        const params = this.props.match.params;

        fetch('/api/1/projects/' + params.id + '/submissions/' + params.submission + '/tests/suites/' + params.suite)
            .then(response => response.json())
            .then(response => this.setState({
                suite: response
            }));
    }

    render() {
        const suite = this.state.suite;

        if (suite === null) {
            return null;
        }

        const cases = this.state.suite.testCases.map(testCase => {
            // Make sure the name is coloured in a way that shows the status of the test case
            const nameClasses = classNames({
                'text-danger': testCase.failed || testCase.errored,
                'text-success': testCase.successful,
                'text-muted': testCase.skipped
            });

            // Colour the duration based on suitable test case lengths
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
