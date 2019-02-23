import React, { Component } from 'react';
import TimeAgo from "react-timeago";

export default class SubmissionListItem extends Component {
    render() {
        const submission = this.props.submission;

        return (
            <tr>
                <td>
                    { submission.externalKey || submission.id }
                </td>
                <td className="text-right">
                    <TimeAgo date={ submission.createdAt }/>
                </td>
            </tr>
        )
    }
}
