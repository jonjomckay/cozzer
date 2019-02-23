import React, { Component } from 'react';
import ProjectListItem from "./ProjectListItem";

export default class ProjectList extends Component {
    state = {
        projects: []
    };

    componentDidMount() {
        fetch('/api/1/projects')
            .then(response => response.json())
            .then(response => this.setState({
                projects: response
            }));
    }

    render() {
        return this.state.projects.map(project => <ProjectListItem key={ project.id } project={ project } />);
    }
}
