/**
 * Created by pctm on 23/06/2017.
 */
import React from 'react';
import {
    StyleSheet,
    Text,
    Button,
    ScrollView,
    View,
    Dimensions,
    TouchableOpacity,
    TouchableHighlight
} from 'react-native'
import Swipeout from 'react-native-swipeout';
import INFO from '../info.json';
import {
    Card,
    CardImage,
    CardTitle,
    CardContent,
    CardAction
} from 'react-native-card-view';
import Toolbar from './dumb/toolbar';


export default class Menu extends React.Component {

    constructor(props) {
        super(props);
    }

    renderClasses() {
        let classes = [];

        for (let i = 0; i < INFO.classes.length; i++) {
            classes.push(<TouchableOpacity key={i} onPress={() => {
                this.props.navigator.push({
                    screen: 'list',
                    passProps: {class: INFO.classes[i]},
                    navigatorStyle: {
                        navBarHidden: true,
                    },
                })
            }}><Text style={{
                backgroundColor: i % 2 == 0 ? "#fffffd" : "white",
                padding: 40, textAlign: "center", color: "gray", fontSize: 22
            }}>{INFO.classes[i].name}</Text></TouchableOpacity>);

            classes.push(<View key={"v" + i} style={{margin: 15, backgroundColor: 'black'}}/>)
        }

        return classes;
    }

    render() {
        return (<View style={{
                flex: 1,
                backgroundColor: 'lightgrey',
            }}>
                <Toolbar style={{marginBottom : 20}} title="Menu" hasLeftButton={false} leftButtonAction={() => this.props.navigator.pop()}/>
                {this.renderClasses()}
            </View>
        );
    }

}