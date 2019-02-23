import React, { Component } from 'react';
import axios from "axios";
import ProjectListItem from "./ProjectListItem";

export default class ProjectList extends Component {
    state = {
        projects: []
    };

    componentDidMount() {
        axios.get('/api/1/projects')
            .then(response => this.setState({
                projects: response.data
            }));
    }

    render() {
        return this.state.projects.map(project => <ProjectListItem key={ project.id } project={ project } />);
    }
}
