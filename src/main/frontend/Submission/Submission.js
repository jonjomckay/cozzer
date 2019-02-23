import React, { Component } from 'react';
import TimeAgo from "react-timeago/lib/index";

export default class Submission extends Component {
    state = {
        submission: null
    };

    componentDidMount() {
        const params = this.props.match.params;

        fetch('/api/1/projects/' + params.id + '/submissions/' + params.submission)
            .then(response => response.json())
            .then(response => this.setState({
                submission: response
            }));
    }

    render() {
        const submission = this.state.submission;

        if (submission === null) {
            return null;
        }

        return (
            <div>
                <h2>{ submission.externalKey || submission.id }</h2>

                <small>
                    Submitted <TimeAgo date={ submission.createdAt } />
                </small>
            </div>
        )
    }
}
