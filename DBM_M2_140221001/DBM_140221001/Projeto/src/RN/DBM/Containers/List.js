/**
 * Created by pctm on 23/06/2017.
 */
import React from 'react';
import {StyleSheet, Text, Button, ScrollView, View, Dimensions, TouchableOpacity} from 'react-native'
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

export default class List extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: []
        };
    }

    add(obj) {
        let data = this.state.data;
        data.push(obj);
        this.setState({data});
    }

    remove(id) {
        fetch(INFO.url + "/api/"+this.props.class.name.toLowerCase()+"/" + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
        }).then((response) => response.json())
            .then((result) => {
                this.componentWillMount();
            });
    }

    componentWillMount() {
        fetch(INFO.url + "/api/"+this.props.class.name.toLowerCase())
            .then((response) => response.json())
            .then((result) => {
                this.setState({data: result});
            });
    }

    mountData() {

        let result = [];
        let {height, width} = Dimensions.get('window');
        for (let i = 0; i < this.state.data.length; i++) {
            let info = [];
            for (let key in this.state.data[i]) {
                info.push(key + ": " + this.state.data[i][key] + "  ");
            }
            let swipeBtns = [
                {
                    text: 'Update',
                    backgroundColor: '#FF9500',
                    underlayColor: 'rgba(0, 0, 0, 1, 0.6)',
                    onPress: () => {
                        this.props.navigator.push({
                            screen: 'edit',
                            passProps: {
                                class : this.props.class,
                                data: this.props.class,
                                add: this.componentWillMount.bind(this),
                                values: this.state.data[i]
                            },
                            navigatorStyle: {
                                navBarHidden: true,
                            },
                        })
                    }
                }, {
                    text: 'Delete',
                    backgroundColor: 'red',
                    underlayColor: 'rgba(0, 0, 0, 1, 0.6)',
                    onPress: () => {
                        this.remove(this.state.data[i].id);
                    }
                }];
            result.push(<Swipeout key={i} right={swipeBtns}
                                  autoClose={true}
                                  backgroundColor='transparent'
                                  buttonWidth={width / 4}>
                <View key={i} style={{
                    flex: 1,
                    backgroundColor: i % 2 == 0 ? "#fffffd" : "white",
                    flexDirection: 'row',
                    padding: 40,
                }}>
                    <View><Text style={{textAlign: 'center', fontSize: 17}}>{info}</Text></View>
                </View>
            </Swipeout>);
            result.push(<View key={"v" + i} style={{margin: 5}}/>)
        }

        return result;

    }

    render() {

        let {height, width} = Dimensions.get('window');
        return (<View style={{flex: 1, backgroundColor: "lightgray"}}>
                <Toolbar style={{marginBottom : 20}} title={"List "+this.props.class.name} hasLeftButton={true} leftButtonAction={() => this.props.navigator.pop()}/>
                <ScrollView>
                    <View style={styles.container}>
                        {this.mountData()}
                    </View>
                </ScrollView>
                <TouchableOpacity onPress={() => {
                    this.props.navigator.push({
                        screen: 'create',
                        passProps: {data: this.props.class, class : this.props.class, add: this.add.bind(this)},
                        navigatorStyle: {
                            navBarHidden: true,
                        },
                    });
                }}><View style={{
                    height: height / 8, backgroundColor: "steelblue", justifyContent: 'center',
                    alignItems: 'center',
                }}>
                    <Text style={{textAlign: 'center', color: "white", fontSize: 20}}>Create</Text>
                </View></TouchableOpacity>
            </View>
        );
    }
}


const styles = StyleSheet.create({
    container: {
        marginTop: 60,
        marginBottom: 60
    },
    title: {
        fontSize: 38,
        backgroundColor: 'transparent'
    },
    button: {
        marginRight: 10
    },
    card: {
        padding: 0,
        margin: 0
    }
});