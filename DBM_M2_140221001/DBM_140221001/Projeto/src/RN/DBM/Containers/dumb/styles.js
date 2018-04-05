/**
 * Created by Dev2grow on 16/05/2017.
 */
import { StyleSheet, Dimensions, Platform } from 'react-native';

const styles = StyleSheet.create({
    flexContainer: {
        flex: 1,
    },
    flexRowContainer: {
        flexDirection: 'row',
    },
    justifyCenter: {
        alignItems: 'center',
    },
    menuButtonIcon: {
        color: 'black',
        fontSize: Dimensions.get('window').height * 0.13,
        marginLeft: 15,
    },
    menuButtonContainer: {
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        padding: 10,
    },
    menuButtonText: {
        marginLeft: 25,
        color: 'white',
        fontSize: 18,
        fontWeight: 'bold',
    },

    dataInputContainer: {
        paddingHorizontal: 20,
        marginVertical: 10,
    },
    dataInputBorder: {
        marginTop: 5,
        borderBottomWidth: 0.5,
        borderBottomColor: '#4D4D4D',
        height: Dimensions.get('window').width * 0.09,
    },
    dataInput: {
        fontSize: Dimensions.get('window').width * 0.03125,
        color: '#4D4D4D'
    },
    inputHeader: {
        fontSize: 14,
        fontWeight: 'bold',
    },

    toolbar: {
        backgroundColor: 'white',
        flexDirection: 'row',
        alignItems: 'center',
        paddingTop: Platform.OS === 'ios' ? 15 : 0,
        height: Dimensions.get('window').height * (Platform.OS === 'ios' ? 0.1 : 0.08),
        marginBottom : 15
    },
    titleContainer:{
        alignItems: 'center'
    },
    leftButtonContainer:{
        flex:1,
        paddingLeft: 15,
        alignItems: 'flex-start'
    },
    rightButtonContainer:{
        flex:1,
        paddingRight: 15,
        alignItems: 'flex-end'
    },
    title: {
        backgroundColor:'transparent',
        fontSize: Dimensions.get('window').width * 0.05,
        color: 'black',
        fontWeight: 'bold',
    },
    leftButton: {
        backgroundColor:'transparent',
        fontSize: Dimensions.get('window').width * (Platform.OS === 'ios' ? 0.06 : 0.06),
        color: 'black',
        fontWeight: 'bold',
    },
    rightButton: {
        backgroundColor:'transparent',
        fontSize: Dimensions.get('window').width * (Platform.OS === 'ios' ? 0.06 : 0.06),
        color: 'black',
        fontWeight: 'bold',
    }
});

export default styles;