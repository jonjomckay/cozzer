import React, { Component } from 'react';
import axios from 'axios';
import SubmissionListItem from "./SubmissionListItem";
import Table from "react-bootstrap/Table";

export default class SubmissionList extends Component {
    state = {
        submissions: []
    };

    componentDidMount() {
        axios.get('/api/1/projects/' + this.props.project + '/submissions')
            .then(response => this.setState({
                submissions: response.data
            }));
    }

    render() {
        const submissions = this.state.submissions
            .map(submission => <SubmissionListItem key={ submission.id } submission={ submission } />);

        return (
            <Table bordered={ false }>
                <tbody>
                { submissions }
                </tbody>
            </Table>
        )
    }
}
