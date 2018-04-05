/**
 * Created by Dev2grow on 01/06/2017.
 */

import React, { Component } from 'react';

import {
    Text,
    TouchableOpacity,
    View,
    Platform,
} from 'react-native';
import Styles from './styles';
import { createIconSetFromIcoMoon } from 'react-native-vector-icons';
import icoMoonConfig from '../../selection.json';
const Icon = createIconSetFromIcoMoon(icoMoonConfig);

export default class Toolbar extends Component {

    constructor(props){
        super(props);
        this.leftButtonAction = this.props.leftButtonAction || this.noAction;
        this.rightButtonAction = this.props.rightButtonAction || this.noAction;
    }

    noAction(){

    }

    render() {

        let leftColor = this.props.hasLeftButton ? {} : {color: 'transparent'};
        let rightColor = this.props.hasRightButton ? {} : {color: 'transparent'};

        return (
            <View style={Styles.toolbar}>
                <TouchableOpacity onPress={() => this.leftButtonAction()} disabled={!this.props.hasLeftButton} style={Styles.leftButtonContainer}>
                    <Icon name={this.props.leftButtonIcon} style={[Styles.leftButton, leftColor]}/>
                </TouchableOpacity>
                <View style={Styles.titleContainer}>
                    <Text style={Styles.title}>{this.props.title}</Text>
                </View>
                <TouchableOpacity onPress={() => this.rightButtonAction()} disabled={!this.props.hasRightButton} style={Styles.rightButtonContainer}>
                    <Icon name={this.props.rightButtonIcon} style={[Styles.rightButton, rightColor]}/>
                </TouchableOpacity>
            </View>
        );
    }
}

Toolbar.defaultProps = {
    leftButtonIcon: 'back',
    rightButtonIcon: 'add',
};