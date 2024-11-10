<template>
	<div class="dashboard-richtext">
		<div class="dashboard-richtext__display" v-if="mode === 'display'" v-html="textData"></div>
		<vue2-tinymce-editor v-else v-model="content" :options="options" @input="editorChange()"></vue2-tinymce-editor>
	</div>
</template>

<script lang="ts">
import { Prop, Component, Vue } from 'nuxt-property-decorator';
//@ts-ignore
import { Vue2TinymceEditor } from "vue2-tinymce-editor";

@Component({
	components: {
		Vue2TinymceEditor
	}
})
export default class DashboardRichTextComponent extends Vue {
	@Prop({default: '', type: String}) public textData!: string;
	@Prop({default: '', type: String}) public mode!: string;
	public content = '';
	public options = {
		menubar: false,
		toolbar1: 'bold italic underline strikethrough forecolor backcolor | alignleft aligncenter alignright alignjustify'
	};

	public editorChange() {
		this.$emit('change', this.content);
	}

	private mounted() {
		this.content = this.textData;
	}
}
</script>

<style lang="scss" scoped>
.dashboard-richtext {
	&__display ::v-deep * {
		all: revert;
	}
}
</style>
