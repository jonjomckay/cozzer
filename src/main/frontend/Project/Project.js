import React, { Component } from 'react';
import SubmissionList from "../SubmissionList";

export default class Project extends Component {
    state = {
        project: null
    };

    componentDidMount() {
        fetch(`/api/1/projects/${ this.props.match.params.id }`)
            .then(response => response.json())
            .then(response => this.setState({
                project: response
            }));
    }

    render() {
        const project = this.state.project;

        if (project === null) {
            return null;
        }

        return (
            <div>
                <h1>{ project.name }</h1>

                <h3>Submissions</h3>

                <SubmissionList project={ project.id } />
            </div>
        )
    }
}
