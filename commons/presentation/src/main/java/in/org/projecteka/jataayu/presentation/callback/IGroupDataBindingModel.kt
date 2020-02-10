package `in`.org.projecteka.jataayu.presentation.callback

interface IGroupDataBindingModel : IDataBindingModel {
    val childrenViewModels: List<IDataBindingModel>?
    val childrenResourceId: Int
    var isExpanded: Boolean
}