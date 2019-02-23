import React from 'react';
import PropTypes from 'prop-types';
import Card from "react-bootstrap/Card";
import { Link } from "react-router-dom";

const ProjectListItem = ({ project }) => (
    <Card>
        <Card.Body>
            <Card.Title>
                <Link to={ '/projects/' + project.id }>{ project.name }</Link>
            </Card.Title>

            <Card.Text>
                Last build submitted February 22nd 2019, 17:45pm
            </Card.Text>
        </Card.Body>
    </Card>
);

ProjectListItem.propTypes = {
    project: PropTypes.object.isRequired
};

export default ProjectListItem;
