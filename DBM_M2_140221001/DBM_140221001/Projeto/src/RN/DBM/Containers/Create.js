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
import t from 'tcomb-form-native';
import {
    Card,
    CardImage,
    CardTitle,
    CardContent,
    CardAction
} from 'react-native-card-view';
import Toolbar from './dumb/toolbar';

var Form = t.form.Form;

export default class Create extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: []
        };
    }

    onPress() {
        // call getValue() to get the values of the form
        var value = this.refs.form.getValue();
        if (value) { // if validation fails, value will be null
            fetch(INFO.url + "/api/" + this.props.class.name.toLowerCase(), {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(value)
            }).then((response) => response.json())
                .then((result) => {
                    this.props.add(result);
                    this.props.navigator.pop();
                });
        }
    }


    getType(type) {
        switch (type) {
            case "String" :
                return t.String;
            case "int" :
                return t.Number;
            default:
                return t.String;
        }
    }

    render() {

        let struct = {}
        for (let i = 0; i < this.props.data.Attributes.length; i++) {
            struct[this.props.data.Attributes[i].name] = this.getType(this.props.data.Attributes[i].type)
        }

        // here we are: define your domain model
        var Person = t.struct(struct);


        return <View>
            <Toolbar style={{marginBottom: 20}} title={"Create " + this.props.class.name} hasLeftButton={true}
                     leftButtonAction={() => this.props.navigator.pop()}/>

            <View style={styles.container}>

                <Form
                    ref="form"
                    type={Person}
                    options={{}}
                />
                <TouchableHighlight style={styles.button} onPress={this.onPress.bind(this)} underlayColor='#99d9f4'>
                    <Text style={styles.buttonText}>Save</Text>
                </TouchableHighlight>
            </View></View>
    }

}


var styles = StyleSheet.create({
    container: {
        justifyContent: 'center',
        backgroundColor: '#ffffff',
    },
    title: {
        fontSize: 30,
        alignSelf: 'center',
        marginBottom: 30
    },
    buttonText: {
        fontSize: 18,
        color: 'white',
        alignSelf: 'center'
    },
    button: {
        height: 36,
        backgroundColor: '#48BBEC',
        borderColor: '#48BBEC',
        borderWidth: 1,
        borderRadius: 8,
        marginBottom: 10,
        alignSelf: 'stretch',
        justifyContent: 'center'
    }
});
