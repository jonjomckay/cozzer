import React, { Component } from 'react';
import SubmissionListItem from "./SubmissionListItem";
import Table from "react-bootstrap/Table";

export default class SubmissionList extends Component {
    state = {
        submissions: []
    };

    componentDidMount() {
        fetch('/api/1/projects/' + this.props.project + '/submissions')
            .then(response => response.json())
            .then(response => this.setState({
                submissions: response
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
