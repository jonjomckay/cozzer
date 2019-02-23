import React, { Component } from 'react';
import TimeAgo from "react-timeago";
import { Link } from "react-router-dom";

export default class SubmissionListItem extends Component {
    render() {
        const submission = this.props.submission;

        return (
            <tr>
                <td>
                    <Link to={ '/projects/' + this.props.project + '/submissions/' + submission.id }>
                        { submission.externalKey || submission.id }
                    </Link>
                </td>
                <td className="text-right">
                    <TimeAgo date={ submission.createdAt }/>
                </td>
            </tr>
        )
    }
}
