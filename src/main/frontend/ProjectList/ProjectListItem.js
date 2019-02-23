import React from 'react';
import PropTypes from 'prop-types';
import Card from "react-bootstrap/Card";
import { Link } from "react-router-dom";
import TimeAgo from "react-timeago";

const ProjectListItem = ({ project }) => (
    <Card>
        <Card.Body>
            <Card.Title>
                <Link to={ '/projects/' + project.id }>{ project.name }</Link>
            </Card.Title>

            <Card.Text>
                Last build submitted <TimeAgo date={ project.lastSubmissionAt } />
            </Card.Text>
        </Card.Body>
    </Card>
);

ProjectListItem.propTypes = {
    project: PropTypes.object.isRequired
};

export default ProjectListItem;
