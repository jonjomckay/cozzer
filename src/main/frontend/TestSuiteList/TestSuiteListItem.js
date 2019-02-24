import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Link } from "react-router-dom";

class TestSuiteListItem extends Component {
    render() {
        const suite = this.props.suite;

        return (
            <tr>
                <td>
                    <Link to={ '/projects/' + this.props.project + '/submissions/' + this.props.submission + '/tests/suites/' + suite.id }>
                        { suite.name }
                    </Link>
                </td>
                <td>{ suite.duration }</td>
                <td>{ suite.numberOfTestCases }</td>
            </tr>
        )
    }
}

TestSuiteListItem.propTypes = {
    project: PropTypes.string.isRequired,
    submission: PropTypes.string.isRequired,
    suite: PropTypes.shape({
        duration: PropTypes.number.isRequired,
        name: PropTypes.string.isRequired,
        numberOfTestCases: PropTypes.number.isRequired
    }).isRequired
};

export default TestSuiteListItem;
