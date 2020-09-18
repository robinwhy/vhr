<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="Department" name="depmana"><DepMana></DepMana></el-tab-pane>
            <el-tab-pane label="Position" name="posmana"><PosMana></PosMana></el-tab-pane>
            <el-tab-pane label="Job level" name="joblevelmana"><JobLevelMana></JobLevelMana></el-tab-pane>
            <el-tab-pane label="Bonus/Penalty" name="ecmana"><EcMana></EcMana></el-tab-pane>
            <el-tab-pane label="Permission" name="permissmana"><PermissMana></PermissMana></el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import DepMana from '../../components/sys/basic/DepMana'
    import PosMana from '../../components/sys/basic/PosMana'
    import JobLevelMana from '../../components/sys/basic/JobLevelMana'
    import EcMana from '../../components/sys/basic/EcMana'
    import PermissMana from '../../components/sys/basic/PermissMana'
    export default {
        name: "SysBasic",
        data(){
            return{
                activeName: 'depmana'
            }
        },
        components:{
            DepMana,
            PosMana,
            JobLevelMana,
            EcMana,
            PermissMana
        }
    }
</script>

<style scoped>

</style>