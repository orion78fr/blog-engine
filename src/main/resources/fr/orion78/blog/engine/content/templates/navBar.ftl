<#macro printCat cat>
  <#if cat.fullPath == catToHighlight>
  <li class="active">
  <#else>
  <li>
  </#if>
  <a href="/category/${cat.fullPath}">
  ${cat.name}
  </a>

  <#list cat.subCategories>
    <ul>
      <#items as subcat>
        <@printCat cat=subcat />
      </#items>
    </ul>
  </#list>
</li>
</#macro>

<ul class="nav nav-pills nav-justified">
<#list categories as category>
  <@printCat cat=category />
</#list>
</ul>
